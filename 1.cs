using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;
using System.Xml;
using System.Xml.Linq;

// Задание 1: Система логирования с уровнями (Logger)
public class Logger
{
    private string _basePath = "app.log";
    private const long MaxFileSize = 1024 * 1024; // 1 MB

    public void Info(string message) => Log("INFO", message);
    public void Warning(string message) => Log("WARNING", message);
    public void Error(string message) => Log("ERROR", message);

    private void Log(string level, string message)
    {
        try
        {
            string currentPath = GetCurrentLogPath();
            string logEntry = $"[{DateTime.Now:yyyy-MM-dd HH:mm:ss}] [{level}] {message}{Environment.NewLine}";
            File.AppendAllText(currentPath, logEntry);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Ошибка логирования: {ex.Message}");
        }
    }

    private string GetCurrentLogPath()
    {
        string path = _basePath;
        int counter = 0;

        while (File.Exists(path))
        {
            FileInfo fileInfo = new FileInfo(path);
            if (fileInfo.Length < MaxFileSize)
                break;

            counter++;
            path = $"app_{counter}.log";
        }

        return path;
    }
}

// Задание 2: Менеджер пользователей (JSON + CRUD)
public class User
{
    public int Id { get; set; }
    public string Name { get; set; }
    public int Age { get; set; }
}

public class UserManager
{
    private List<User> _users = new List<User>();
    private string _filePath = "users.json";

    public UserManager() => LoadUsers();

    public void AddUser(User user)
    {
        if (_users.Any(u => u.Id == user.Id))
            throw new InvalidOperationException("Пользователь с таким Id уже существует");

        _users.Add(user);
        SaveUsers();
    }

    public void RemoveUser(int id)
    {
        var user = _users.FirstOrDefault(u => u.Id == id);
        if (user != null)
        {
            _users.Remove(user);
            SaveUsers();
        }
    }

    public void UpdateUser(User updatedUser)
    {
        var user = _users.FirstOrDefault(u => u.Id == updatedUser.Id);
        if (user != null)
        {
            user.Name = updatedUser.Name;
            user.Age = updatedUser.Age;
            SaveUsers();
        }
    }

    private void LoadUsers()
    {
        try
        {
            if (File.Exists(_filePath))
            {
                string json = File.ReadAllText(_filePath);
                _users = JsonSerializer.Deserialize<List<User>>(json) ?? new List<User>();
            }
            else
            {
                _users = new List<User>();
                SaveUsers();
            }
        }
        catch
        {
            _users = new List<User>();
        }
    }

    private void SaveUsers()
    {
        string json = JsonSerializer.Serialize(_users, new JsonSerializerOptions { WriteIndented = true });
        File.WriteAllText(_filePath, json);
    }
}

// Задание 3: Анализатор логов (TXT → аналитика)
public class LogAnalyzer
{
    public void AnalyzeLogs(string inputPath, string outputPath)
    {
        try
        {
            var counts = new Dictionary<string, int> { { "INFO", 0 }, { "WARNING", 0 }, { "ERROR", 0 } };

            foreach (var line in File.ReadLines(inputPath))
            {
                if (string.IsNullOrWhiteSpace(line))
                    continue;

                string[] parts = line.Split(':', 2);
                if (parts.Length == 2)
                {
                    string level = parts[0].ToUpperInvariant();
                    if (counts.ContainsKey(level))
                        counts[level]++;
                }
            }

            string mostFrequent = counts.OrderByDescending(kv => kv.Value).First().Key;
            string report = $"INFO: {counts["INFO"]}\nWARNING: {counts["WARNING"]}\nERROR: {counts["ERROR"]}\nСамый частый: {mostFrequent}";
            File.WriteAllText(outputPath, report);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Ошибка анализа логов: {ex.Message}");
        }
    }
}

// Задание 4: Резервное копирование директории
public class BackupManager
{
    public void CreateBackup(string sourceDir, string backupDir)
    {
        try
        {
            backupDir = Path.Combine(backupDir, $"backup_{DateTime.Now:yyyyMMdd_HHmmss}");
            Directory.CreateDirectory(backupDir);
            CopyDirectory(sourceDir, backupDir);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Ошибка резервного копирования: {ex.Message}");
        }
    }

    private void CopyDirectory(string source, string destination)
    {
        Directory.CreateDirectory(destination);

        foreach (string file in Directory.GetFiles(source))
        {
            try
            {
                File.Copy(file, Path.Combine(destination, Path.GetFileName(file)), true);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Ошибка копирования файла {file}: {ex.Message}");
            }
        }

        foreach (string dir in Directory.GetDirectories(source))
            CopyDirectory(dir, Path.Combine(destination, Path.GetFileName(dir)));
    }
}

// Задание 5: Конвертер JSON → XML
public class JsonToXmlConverter
{
    public void Convert(string jsonPath, string xmlPath)
    {
        try
        {
            string json = File.ReadAllText(jsonPath);
            var jsonDoc = JsonDocument.Parse(json);
            XElement xmlRoot = ConvertJsonToXml(jsonDoc.RootElement, "Product");
            xmlRoot.Save(xmlPath);
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Ошибка конвертации JSON в XML: {ex.Message}");
        }
    }

    private XElement ConvertJsonToXml(JsonElement element, string elementName)
    {
        XElement xmlElement = new XElement(elementName);

        switch (element.ValueKind)
        {
            case JsonValueKind.Object:
                foreach (var property in element.EnumerateObject())
                    xmlElement.Add(ConvertJsonToXml(property.Value, property.Name));
                break;
            case JsonValueKind.Array:
                foreach (var item in element.EnumerateArray())
                    xmlElement.Add(ConvertJsonToXml(item, "Item"));
                break;
            default:
                xmlElement.Value = element.GetString() ?? string.Empty;
                break;
        }

        return xmlElement;
    }
}

// Задание 6: Мониторинг папки (Watcher)
public class FolderWatcher
{
    public void StartMonitoring(string folderPath)
    {
        FileSystemWatcher watcher = new FileSystemWatcher(folderPath);
        watcher.Filter = "*.txt";
        watcher.Created += (s, e) => LogEvent($"Создан файл: {e.Name}");
        watcher.Deleted += (s, e) => LogEvent($"Удалён файл: {e.Name}");
        watcher.Changed += (s, e) => LogEvent($"Изменён файл: {e.Name}");
        watcher.EnableRaisingEvents = true;
    }

    private void LogEvent(string message)
    {
        Console.WriteLine(message);
        File.AppendAllText("watcher_log.txt", $"{DateTime.Now}: {message}{Environment.NewLine}");
    }
}
