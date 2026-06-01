<Window x:Class="WpfApp3.MainWindow"
        xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
        xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
        Title="Работа с данными"
        Height="450"
        Width="800">

    <Grid Margin="10">

        <Grid.RowDefinitions>
            <RowDefinition Height="Auto"/>
            <RowDefinition Height="2*"/>
            <RowDefinition Height="*"/>
        </Grid.RowDefinitions>

        <!-- Фильтр -->
        <ComboBox Name="FilterComboBox"
                  Width="150"
                  SelectionChanged="FilterComboBox_SelectionChanged">
            <ComboBoxItem Content="Все" IsSelected="True"/>
            <ComboBoxItem Content="IT"/>
            <ComboBoxItem Content="Бухгалтерия"/>
        </ComboBox>

        <!-- Таблица -->
        <DataGrid Name="EmployeeDataGrid"
                  Grid.Row="1"
                  AutoGenerateColumns="True"
                  CanUserAddRows="True"
                  Margin="0,10,0,10">

            <DataGrid.ContextMenu>
                <ContextMenu>
                    <MenuItem Header="Удалить"
                              Click="DeleteMenuItem_Click"/>
                </ContextMenu>
            </DataGrid.ContextMenu>

        </DataGrid>

        <!-- Список -->
        <ListView Name="EmployeeListView"
                  Grid.Row="2"/>
    </Grid>
</Window>


using System.Collections.ObjectModel;
using System.Linq;
using System.Windows;
using System.Windows.Controls;

namespace WpfApp3
{
    public partial class MainWindow : Window
    {
        private ObservableCollection<Employee> employees;

        public MainWindow()
        {
            InitializeComponent();

            employees = new ObservableCollection<Employee>
            {
                new Employee { Id = 1, Name = "Иван", Department = "IT" },
                new Employee { Id = 2, Name = "Петр", Department = "Бухгалтерия" },
                new Employee { Id = 3, Name = "Анна", Department = "IT" }
            };

            EmployeeDataGrid.ItemsSource = employees;
            EmployeeListView.ItemsSource = employees;
        }

        private void FilterComboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (EmployeeDataGrid == null)
                return;

            string filter =
                ((ComboBoxItem)FilterComboBox.SelectedItem).Content.ToString();

            if (filter == "Все")
            {
                EmployeeDataGrid.ItemsSource = employees;
                EmployeeListView.ItemsSource = employees;
            }
            else
            {
                var filtered = employees
                    .Where(x => x.Department == filter)
                    .ToList();

                EmployeeDataGrid.ItemsSource = filtered;
                EmployeeListView.ItemsSource = filtered;
            }
        }

        private void DeleteMenuItem_Click(object sender, RoutedEventArgs e)
        {
            Employee employee =
                EmployeeDataGrid.SelectedItem as Employee;

            if (employee != null)
            {
                employees.Remove(employee);
            }
        }
    }

    public class Employee
    {
        public int Id { get; set; }

        public string Name { get; set; }

        public string Department { get; set; }
    }
}
