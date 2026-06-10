<Window x:Class="NavigationModule.MainWindow"
        xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
        xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
        Title="Главное меню" Height="450" Width="800">

    <DockPanel>

        <!-- Верхнее меню -->
        <Menu DockPanel.Dock="Top">
            <MenuItem Header="Файл">
                <MenuItem Header="Главная" Click="MainPage_Click"/>
                <MenuItem Header="Настройки" Click="SettingsPage_Click"/>
            </MenuItem>

            <MenuItem Header="Справка">
                <MenuItem Header="О программе" Click="AboutPage_Click"/>
            </MenuItem>
        </Menu>

        <Grid>
            <Grid.ColumnDefinitions>
                <ColumnDefinition Width="200"/>
                <ColumnDefinition Width="*"/>
            </Grid.ColumnDefinitions>

            <!-- Боковое меню -->
            <ListBox Name="NavigationList"
                     SelectionChanged="NavigationList_SelectionChanged">
                <ListBoxItem Content="Главная"/>
                <ListBoxItem Content="Настройки"/>
                <ListBoxItem Content="О программе"/>
            </ListBox>

            <!-- Область навигации -->
            <TabControl Grid.Column="1">
                <TabItem Header="Содержимое">
                    <Frame Name="MainFrame"
                           NavigationUIVisibility="Hidden"/>
                </TabItem>
            </TabControl>

        </Grid>

    </DockPanel>
</Window>



using System.Windows;
using System.Windows.Controls;

namespace NavigationModule
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            MainFrame.Navigate(new MainPage());
        }

        private void NavigationList_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (NavigationList.SelectedItem is ListBoxItem item)
            {
                switch (item.Content.ToString())
                {
                    case "Главная":
                        MainFrame.Navigate(new MainPage());
                        break;

                    case "Настройки":
                        MainFrame.Navigate(new SettingsPage());
                        break;

                    case "О программе":
                        MainFrame.Navigate(new AboutPage());
                        break;
                }
            }
        }

        private void MainPage_Click(object sender, RoutedEventArgs e)
        {
            MainFrame.Navigate(new MainPage());
        }

        private void SettingsPage_Click(object sender, RoutedEventArgs e)
        {
            MainFrame.Navigate(new SettingsPage());
        }

        private void AboutPage_Click(object sender, RoutedEventArgs e)
        {
            MainFrame.Navigate(new AboutPage());
        }
    }
}



<Page x:Class="NavigationModule.MainPage"
      xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
      xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml">

    <Grid>
        <TextBlock Text="Главная страница"
                   FontSize="24"
                   HorizontalAlignment="Center"
                   VerticalAlignment="Center"/>
    </Grid>

</Page>





<Page x:Class="NavigationModule.SettingsPage"
      xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
      xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml">

    <Grid>
        <TextBlock Text="Настройки"
                   FontSize="24"
                   HorizontalAlignment="Center"
                   VerticalAlignment="Center"/>
    </Grid>

</Page>






<Page x:Class="NavigationModule.AboutPage"
      xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
      xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml">

    <Grid>
        <TextBlock Text="О программе"
                   FontSize="24"
                   HorizontalAlignment="Center"
                   VerticalAlignment="Center"/>
    </Grid>

</Page>






using System;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using Newtonsoft.Json;

namespace WpfAuthApp
{
    public partial class LoginWindow : Window
    {
        public LoginWindow()
        {
            InitializeComponent();
        }

        private async void BtnLogin_Click(object sender, RoutedEventArgs e)
        {
            string login = txtLogin.Text;
            string password = txtPassword.Password;

            if (string.IsNullOrEmpty(login) || string.IsNullOrEmpty(password))
            {
                lblError.Text = "Введите логин и пароль";
                return;
            }

            btnLogin.IsEnabled = false;
            lblError.Text = "Выполняется вход...";

            bool success = await Authenticate(login, password);

            if (success)
            {
                // Открываем главное окно с данными пользователя
                MainWindow mainWindow = new MainWindow();
                mainWindow.Show();
                this.Close();
            }
            else
            {
                lblError.Text = "Неверный логин или пароль";
                btnLogin.IsEnabled = true;
            }
        }

        private async Task<bool> Authenticate(string login, string password)
        {
            using (HttpClient client = new HttpClient())
            {
                var loginData = new { Username = login, Password = password };
                string json = JsonConvert.SerializeObject(loginData);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                try
                {
                    // Замените URL на ваш реальный API
                    HttpResponseMessage response = await client.PostAsync("https://your-api.com/api/auth/login", content);
                    
                    if (response.IsSuccessStatusCode)
                    {
                        string result = await response.Content.ReadAsStringAsync();
                        dynamic data = JsonConvert.DeserializeObject(result);
                        
                        // Сохраняем токен и данные пользователя
                        Properties.Settings.Default.Token = data.token;
                        Properties.Settings.Default.UserData = result;
                        Properties.Settings.Default.Save();
                        
                        return true;
                    }
                    return false;
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"Ошибка подключения: {ex.Message}");
                    return false;
                }
            }
        }
    }
}
