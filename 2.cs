using System;
using System.Collections.Generic;
using System.IO;
using System.Text.Json;
using Microsoft.Extensions.DependencyInjection;

// Модель
public class Order
{
    public int Id { get; set; }
    public string Product { get; set; }
    public double Price { get; set; }
}

// Интерфейсы
public interface IOrderRepository
{
    void Save(Order order);
    List<Order> GetAll();
}

public interface ILogger
{
    void Log(string message);
}

public interface IOrderService
{
    void CreateOrder(Order order);
    List<Order> GetOrders();
    void DeleteOrder(int id);
}

// Реализации
public class FileOrderRepository : IOrderRepository
{
    private const string FilePath = "orders.json";

    public List<Order> GetAll()
    {
        try
        {
            if (!File.Exists(FilePath))
                return new List<Order>();

            var json = File.ReadAllText(FilePath);
            return JsonSerializer.Deserialize<List<Order>>(json) ?? new List<Order>();
        }
        catch (Exception ex)
        {
            throw new InvalidOperationException($"Ошибка чтения файла: {ex.Message}");
        }
    }

    public void Save(Order order)
    {
        var orders = GetAll();
        orders.Add(order);
        var json = JsonSerializer.Serialize(orders, new JsonSerializerOptions { WriteIndented = true });
        File.WriteAllText(FilePath, json);
    }
}

public class MemoryOrderRepository : IOrderRepository
{
    private List<Order> _orders = new List<Order>();
    private int _nextId = 1;

    public List<Order> GetAll() => _orders;

    public void Save(Order order)
    {
        order.Id = _nextId++;
        _orders.Add(order);
    }
}

public class FileLogger : ILogger
{
    private const string LogPath = "app.log";

    public void Log(string message)
    {
        var logEntry = $"[{DateTime.Now:yyyy-MM-dd HH:mm:ss}] {message}";
        File.AppendAllText(LogPath, logEntry + Environment.NewLine);
    }
}

public class ConsoleLogger : ILogger
{
    public void Log(string message)
    {
        Console.WriteLine($"[{DateTime.Now:HH:mm:ss}] {message}");
    }
}

public class OrderService : IOrderService
{
    private readonly IOrderRepository _repository;
    private readonly ILogger _logger;

    public OrderService(IOrderRepository repository, ILogger logger)
    {
        _repository = repository;
        _logger = logger;
    }

    public void CreateOrder(Order order)
    {
        if (order.Price <= 0)
            throw new ArgumentException("Цена должна быть больше 0");

        _repository.Save(order);
        _logger.Log($"Создан заказ: {order.Product}, цена: {order.Price}");
    }

    public List<Order> GetOrders() => _repository.GetAll();

    public void DeleteOrder(int id)
    {
        var orders = _repository.GetAll();
        var order = orders.Find(o => o.Id == id);
        if (order != null)
        {
            orders.Remove(order);
            var tempRepo = new MemoryOrderRepository();
            foreach (var o in orders)
                tempRepo.Save(o);

            File.WriteAllText("orders.json", JsonSerializer.Serialize(tempRepo.GetAll(),
                new JsonSerializerOptions { WriteIndented = true }));
            _logger.Log($"Удален заказ с ID: {id}");
        }
        else
        {
            _logger.Log($"Попытка удаления несуществующего заказа с ID: {id}");
            throw new KeyNotFoundException("Заказ не найден");
        }
    }
}

class Program
{
    static void Main()
    {
        var services = new ServiceCollection();

        services.AddTransient<IOrderRepository, FileOrderRepository>();
        services.AddSingleton<ILogger, FileLogger>();
        services.AddTransient<IOrderService, OrderService>();

        var provider = services.BuildServiceProvider();
        var service = provider.GetService<IOrderService>();
        var logger = provider.GetService<ILogger>();

        while (true)
        {
            Console.WriteLine("\n1. Добавить заказ");
            Console.WriteLine("2. Показать заказы");
            Console.WriteLine("3. Удалить заказ");
            Console.WriteLine("4. Выход");
            Console.Write("Выберите действие: ");

            var choice = Console.ReadLine();

            try
            {
                switch (choice)
                {
                    case "1":
                        Console.Write("Продукт: ");
                        var product = Console.ReadLine();
                        Console.Write("Цена: ");
                        if (double.TryParse(Console.ReadLine(), out double price))
                        {
                            var order = new Order { Product = product, Price = price };
                            service.CreateOrder(order);
                            Console.WriteLine("Заказ успешно создан!");
                        }
                        else
                        {
                            Console.WriteLine("Некорректная цена!");
                        }
                        break;

                    case "2":
                        var orders = service.GetOrders();
                        Console.WriteLine("\nСписок заказов:");
                        foreach (var order in orders)
                        {
                            Console.WriteLine($"ID: {order.Id}, Продукт: {order.Product}, Цена: {order.Price}");
                        }
                        break;

                    case "3":
                        Console.Write("Введите ID заказа для удаления: ");
                        if (int.TryParse(Console.ReadLine(), out int id))
                        {
                            service.DeleteOrder(id);
                            Console.WriteLine("Заказ удален!");
                        }
                        else
                        {
                            Console.WriteLine("Некорректный ID!");
                        }
                        break;

                    case "4":
                        return;

                    default:
                        Console.WriteLine("Неверный выбор!");
                        break;
                }
            }
            catch (Exception ex)
            {
                logger.Log($"Ошибка: {ex.Message}");
                Console.WriteLine($"Ошибка: {ex.Message}");
            }
        }
    }
}
```
