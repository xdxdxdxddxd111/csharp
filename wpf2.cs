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
