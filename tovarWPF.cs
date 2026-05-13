using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Input;
using System.Windows.Media;

namespace WpfApp3
{
    public class Program
    {
        [STAThread]
        public static void Main()
        {
            Application app = new Application();
            app.Run(new MainWindow());
        }
    }

    public class MainWindow : Window
    {
        private CatalogViewModel _viewModel;
        private Grid _mainGrid;

        public MainWindow()
        {
            this.Title = "Каталог товаров с корзиной (.NET 4.8.1)";
            this.Height = 650;
            this.Width = 1000; // Немного увеличили ширину для кнопки удаления
            this.MinWidth = 900;

            _viewModel = new CatalogViewModel();
            this.DataContext = _viewModel;

            _mainGrid = new Grid();
            _mainGrid.Margin = new Thickness(10);
            this.Content = _mainGrid;

            BuildInterface();
        }

        private void BuildInterface()
        {
            ColumnDefinition colDef1 = new ColumnDefinition();
            colDef1.Width = new GridLength(220, GridUnitType.Pixel);
            _mainGrid.ColumnDefinitions.Add(colDef1);

            ColumnDefinition colDef2 = new ColumnDefinition();
            colDef2.Width = new GridLength(1, GridUnitType.Star);
            _mainGrid.ColumnDefinitions.Add(colDef2);

            ColumnDefinition colDef3 = new ColumnDefinition();
            colDef3.Width = new GridLength(300, GridUnitType.Pixel); // Увеличили под DataGrid с кнопкой
            _mainGrid.ColumnDefinitions.Add(colDef3);

            #region ЛЕВАЯ ПАНЕЛЬ: ФИЛЬТРЫ
            GroupBox filterGroup = new GroupBox();
            filterGroup.Header = "Фильтрация товаров";
            filterGroup.Margin = new Thickness(0, 0, 10, 0);
            filterGroup.Padding = new Thickness(8);

            StackPanel filterStack = new StackPanel();

            TextBlock lblCat = new TextBlock();
            lblCat.Text = "Категория:";
            lblCat.FontWeight = FontWeights.SemiBold;
            lblCat.Margin = new Thickness(0, 5, 0, 3);
            filterStack.Children.Add(lblCat);

            ComboBox catCombo = new ComboBox();
            catCombo.SetBinding(ComboBox.ItemsSourceProperty, new Binding("FilterCategories"));
            catCombo.SetBinding(ComboBox.SelectedItemProperty, new Binding("SelectedCategory"));
            filterStack.Children.Add(catCombo);

            TextBlock lblPrice = new TextBlock();
            lblPrice.Text = "Макс. цена:";
            lblPrice.FontWeight = FontWeights.SemiBold;
            lblPrice.Margin = new Thickness(0, 15, 0, 3);
            filterStack.Children.Add(lblPrice);

            Slider priceSlider = new Slider();
            priceSlider.Minimum = 0;
            priceSlider.Maximum = 150000;
            priceSlider.TickFrequency = 5000;
            priceSlider.IsSnapToTickEnabled = true;
            priceSlider.SetBinding(Slider.ValueProperty, new Binding("MaxPrice") { UpdateSourceTrigger = UpdateSourceTrigger.PropertyChanged });
            filterStack.Children.Add(priceSlider);

            TextBlock priceLabel = new TextBlock();
            priceLabel.HorizontalAlignment = HorizontalAlignment.Right;
            priceLabel.Foreground = Brushes.DarkBlue;
            priceLabel.FontWeight = FontWeights.Bold;
            priceLabel.SetBinding(TextBlock.TextProperty, new Binding("MaxPrice") { StringFormat = "{0:C}", ConverterCulture = new CultureInfo("ru-RU") });
            filterStack.Children.Add(priceLabel);

            CheckBox inStockCheck = new CheckBox();
            inStockCheck.Content = "Только в наличии";
            inStockCheck.Margin = new Thickness(0, 15, 0, 0);
            inStockCheck.FontWeight = FontWeights.SemiBold;
            inStockCheck.SetBinding(CheckBox.IsCheckedProperty, new Binding("OnlyInStock"));
            filterStack.Children.Add(inStockCheck);

            filterGroup.Content = filterStack;
            Grid.SetColumn(filterGroup, 0);
            _mainGrid.Children.Add(filterGroup);
            #endregion

            #region ЦЕНТРАЛЬНАЯ ПАНЕЛЬ: КАТАЛОГ (ListView)
            GroupBox catalogGroup = new GroupBox();
            catalogGroup.Header = "Каталог продукции";
            catalogGroup.Margin = new Thickness(0, 0, 10, 0);
            catalogGroup.Padding = new Thickness(5);

            ListView catalogListView = new ListView();
            catalogListView.HorizontalContentAlignment = HorizontalAlignment.Stretch;
            ScrollViewer.SetHorizontalScrollBarVisibility(catalogListView, ScrollBarVisibility.Disabled);
            catalogListView.SetBinding(ListView.ItemsSourceProperty, new Binding("FilteredProducts"));

            FrameworkElementFactory panelFactory = new FrameworkElementFactory(typeof(WrapPanel));
            catalogListView.ItemsPanel = new ItemsPanelTemplate(panelFactory);

            FrameworkElementFactory cardBorder = new FrameworkElementFactory(typeof(Border));
            cardBorder.SetValue(Border.BorderBrushProperty, new SolidColorBrush((Color)ColorConverter.ConvertFromString("#CCCCCC")));
            cardBorder.SetValue(Border.BorderThicknessProperty, new Thickness(1));
            cardBorder.SetValue(Border.CornerRadiusProperty, new CornerRadius(5));
            cardBorder.SetValue(Border.MarginProperty, new Thickness(6));
            cardBorder.SetValue(Border.PaddingProperty, new Thickness(8));
            cardBorder.SetValue(Border.WidthProperty, 180.0);
            cardBorder.SetValue(Border.HeightProperty, 240.0);
            cardBorder.SetValue(Border.BackgroundProperty, Brushes.White);

            FrameworkElementFactory cardGrid = new FrameworkElementFactory(typeof(Grid));

            FrameworkElementFactory titleFactory = new FrameworkElementFactory(typeof(TextBlock));
            titleFactory.SetValue(TextBlock.FontWeightProperty, FontWeights.Bold);
            titleFactory.SetValue(TextBlock.FontSizeProperty, 14.0);
            titleFactory.SetValue(TextBlock.TextTrimmingProperty, TextTrimming.CharacterEllipsis);
            titleFactory.SetBinding(TextBlock.TextProperty, new Binding("Name"));
            cardGrid.AppendChild(titleFactory);

            FrameworkElementFactory descFactory = new FrameworkElementFactory(typeof(TextBlock));
            descFactory.SetValue(TextBlock.FontSizeProperty, 11.0);
            descFactory.SetValue(TextBlock.ForegroundProperty, Brushes.Gray);
            descFactory.SetValue(TextBlock.TextWrappingProperty, TextWrapping.Wrap);
            descFactory.SetValue(TextBlock.TextTrimmingProperty, TextTrimming.CharacterEllipsis);
            descFactory.SetValue(TextBlock.MarginProperty, new Thickness(0, 25, 0, 0));
            descFactory.SetBinding(TextBlock.TextProperty, new Binding("Description"));
            cardGrid.AppendChild(descFactory);

            FrameworkElementFactory priceFactory = new FrameworkElementFactory(typeof(TextBlock));
            priceFactory.SetValue(TextBlock.FontWeightProperty, FontWeights.SemiBold);
            priceFactory.SetValue(TextBlock.ForegroundProperty, Brushes.DarkRed);
            priceFactory.SetValue(TextBlock.VerticalAlignmentProperty, VerticalAlignment.Bottom);
            priceFactory.SetValue(TextBlock.MarginProperty, new Thickness(0, 0, 0, 35));
            priceFactory.SetBinding(TextBlock.TextProperty, new Binding("Price") { StringFormat = "{0:C}", ConverterCulture = new CultureInfo("ru-RU") });
            cardGrid.AppendChild(priceFactory);

            FrameworkElementFactory buyBtnFactory = new FrameworkElementFactory(typeof(Button));
            buyBtnFactory.SetValue(Button.ContentProperty, "В корзину");
            buyBtnFactory.SetValue(Button.HeightProperty, 25.0);
            buyBtnFactory.SetValue(Button.VerticalAlignmentProperty, VerticalAlignment.Bottom);
            buyBtnFactory.SetValue(Button.BackgroundProperty, new SolidColorBrush((Color)ColorConverter.ConvertFromString("#FFF0C2")));
            buyBtnFactory.SetBinding(Button.CommandProperty, new Binding("DataContext.AddToCartCommand") { RelativeSource = new RelativeSource(RelativeSourceMode.FindAncestor, typeof(ListView), 1) });
            buyBtnFactory.SetBinding(Button.CommandParameterProperty, new Binding());
            buyBtnFactory.SetBinding(Button.IsEnabledProperty, new Binding("InStock"));
            cardGrid.AppendChild(buyBtnFactory);

            cardBorder.AppendChild(cardGrid);
            catalogListView.ItemTemplate = new DataTemplate { VisualTree = cardBorder };

            catalogGroup.Content = catalogListView;
            Grid.SetColumn(catalogGroup, 1);
            _mainGrid.Children.Add(catalogGroup);
            #endregion

            #region ПРАВАЯ ПАНЕЛЬ: КОРЗИНА С КНОПКОЙ УДАЛЕНИЯ
            GroupBox cartGroup = new GroupBox();
            cartGroup.Header = "Корзина покупателя";
            Grid.SetColumn(cartGroup, 2);
            cartGroup.Padding = new Thickness(5);

            Grid cartGrid = new Grid();
            RowDefinition rowDef1 = new RowDefinition();
            rowDef1.Height = new GridLength(1, GridUnitType.Star);
            cartGrid.RowDefinitions.Add(rowDef1);

            RowDefinition rowDef2 = new RowDefinition();
            rowDef2.Height = GridLength.Auto;
            cartGrid.RowDefinitions.Add(rowDef2);

            DataGrid cartDataGrid = new DataGrid();
            cartDataGrid.AutoGenerateColumns = false;
            cartDataGrid.IsReadOnly = true;
            cartDataGrid.HeadersVisibility = DataGridHeadersVisibility.Column;
            cartDataGrid.GridLinesVisibility = DataGridGridLinesVisibility.Horizontal;
            cartDataGrid.SetBinding(DataGrid.ItemsSourceProperty, new Binding("CartItems"));

            DataGridTextColumn colName = new DataGridTextColumn();
            colName.Header = "Товар";
            colName.Binding = new Binding("Product.Name");
            colName.Width = new DataGridLength(1, DataGridLengthUnitType.Star);
            cartDataGrid.Columns.Add(colName);

            DataGridTextColumn colQty = new DataGridTextColumn();
            colQty.Header = "Кол-во";
            colQty.Binding = new Binding("Quantity");
            colQty.Width = new DataGridLength(45, DataGridLengthUnitType.Pixel);
            cartDataGrid.Columns.Add(colQty);

            DataGridTextColumn colSum = new DataGridTextColumn();
            colSum.Header = "Сумма";
            colSum.Binding = new Binding("TotalPrice") { StringFormat = "{0:C}", ConverterCulture = new CultureInfo("ru-RU") };
            colSum.Width = new DataGridLength(65, DataGridLengthUnitType.Pixel);
            cartDataGrid.Columns.Add(colSum);

            // НОВАЯ КОЛОНКА: Кнопка «Х» (Удалить из корзины) через создание шаблона DataGridTemplateColumn
            DataGridTemplateColumn colDelete = new DataGridTemplateColumn();
            colDelete.Header = "Удалить";
            colDelete.Width = new DataGridLength(55, DataGridLengthUnitType.Pixel);

            FrameworkElementFactory deleteBtnFactory = new FrameworkElementFactory(typeof(Button));
            deleteBtnFactory.SetValue(Button.ContentProperty, "✖");
            deleteBtnFactory.SetValue(Button.ForegroundProperty, Brushes.DarkRed);
            deleteBtnFactory.SetValue(Button.BackgroundProperty, Brushes.Transparent);
            deleteBtnFactory.SetValue(Button.BorderThicknessProperty, new Thickness(0));
            deleteBtnFactory.SetValue(Button.FontWeightProperty, FontWeights.Bold);
            deleteBtnFactory.SetValue(Button.HeightProperty, 20.0);
            deleteBtnFactory.SetValue(Button.WidthProperty, 20.0);
            deleteBtnFactory.SetValue(Button.ToolTipProperty, "Удалить единицу товара");

            // Привязка команды удаления к DataContext главного DataGrid через RelativeSource
            deleteBtnFactory.SetBinding(Button.CommandProperty, new Binding("DataContext.RemoveFromCartCommand") { RelativeSource = new RelativeSource(RelativeSourceMode.FindAncestor, typeof(DataGrid), 1) });
            deleteBtnFactory.SetBinding(Button.CommandParameterProperty, new Binding()); // Передает текущий CartItemModel

            colDelete.CellTemplate = new DataTemplate { VisualTree = deleteBtnFactory };
            cartDataGrid.Columns.Add(colDelete);

            Grid.SetRow(cartDataGrid, 0);
            cartGrid.Children.Add(cartDataGrid);

            StackPanel totalStack = new StackPanel();
            totalStack.Margin = new Thickness(0, 10, 0, 0);

            TextBlock totalText = new TextBlock();
            totalText.FontSize = 15;
            totalText.FontWeight = FontWeights.Bold;
            totalText.HorizontalAlignment = HorizontalAlignment.Right;
            totalText.Margin = new Thickness(0, 0, 0, 5);
            totalText.SetBinding(TextBlock.TextProperty, new Binding("CartTotal") { StringFormat = "Итого: {0:C}", ConverterCulture = new CultureInfo("ru-RU") });
            totalStack.Children.Add(totalText);

            Button checkoutBtn = new Button();
            checkoutBtn.Content = "Оформить заказ";
            checkoutBtn.Height = 30;
            checkoutBtn.Background = new SolidColorBrush((Color)ColorConverter.ConvertFromString("#E8F5E9"));
            checkoutBtn.FontWeight = FontWeights.SemiBold;
            checkoutBtn.SetBinding(Button.CommandProperty, new Binding("CheckoutCommand"));
            totalStack.Children.Add(checkoutBtn);

            Grid.SetRow(totalStack, 1);
            cartGrid.Children.Add(totalStack);

            cartGroup.Content = cartGrid;
            _mainGrid.Children.Add(cartGroup);
            #endregion
        }
    }

    #region MVVM МОДЕЛИ И ЛОГИКА
    public class ProductModel
    {
        public string Name { get; set; }
        public string Category { get; set; }
        public double Price { get; set; }
        public string Description { get; set; }
        public bool InStock { get; set; }
    }

    public class CartItemModel : INotifyPropertyChanged
    {
        private int _quantity;
        public ProductModel Product { get; set; }
        public int Quantity
        {
            get { return _quantity; }
            set { _quantity = value; OnPropertyChanged(); OnPropertyChanged("TotalPrice"); }
        }
        public double TotalPrice { get { return Product.Price * Quantity; } }

        public event PropertyChangedEventHandler PropertyChanged;
        protected void OnPropertyChanged([CallerMemberName] string name = null)
        {
            if (PropertyChanged != null) PropertyChanged(this, new PropertyChangedEventArgs(name));
        }
    }

    public class RelayCommand : ICommand
    {
        private readonly Action<object> _execute;
        private readonly Func<object, bool> _canExecute;
        public RelayCommand(Action<object> execute, Func<object, bool> canExecute = null) { _execute = execute; _canExecute = canExecute; }
        public bool CanExecute(object parameter) { return _canExecute == null || _canExecute(parameter); }
        public void Execute(object parameter) { _execute(parameter); }
        public event EventHandler CanExecuteChanged { add { CommandManager.RequerySuggested += value; } remove { CommandManager.RequerySuggested -= value; } }
    }

    public class CatalogViewModel : INotifyPropertyChanged
    {
        private string _selectedCategory = "Все категории";
        private double _maxPrice = 150000;
        private bool _onlyInStock = false;
        private double _cartTotal = 0;

        public ObservableCollection<ProductModel> AllProducts { get; set; }
        public ICollectionView FilteredProducts { get; set; }
        public ObservableCollection<CartItemModel> CartItems { get; set; }
        public ObservableCollection<string> FilterCategories { get; set; }

        public ICommand AddToCartCommand { get; set; }
        public ICommand RemoveFromCartCommand { get; set; } // НОВАЯ КОМАНДА
        public ICommand CheckoutCommand { get; set; }

        public CatalogViewModel()
        {
            AllProducts = new ObservableCollection<ProductModel>();
            CartItems = new ObservableCollection<CartItemModel>();
            FilterCategories = new ObservableCollection<string> { "Все категории", "Смартфоны", "Ноутбуки", "Аксессуары" };

            AllProducts.Add(new ProductModel { Name = "iPhone 15 Pro", Category = "Смартфоны", Price = 120000, Description = "Титан, чип A17 Pro.", InStock = true });
            AllProducts.Add(new ProductModel { Name = "Samsung Galaxy S24", Category = "Смартфоны", Price = 95000, Description = "Продвинутая камера с ИИ.", InStock = true });
            AllProducts.Add(new ProductModel { Name = "MacBook Pro 16", Category = "Ноутбуки", Price = 150000, Description = "M3 Max для сложных задач.", InStock = false });
            AllProducts.Add(new ProductModel { Name = "ASUS ROG Gaming", Category = "Ноутбуки", Price = 145000, Description = "144Гц экран, RTX серия.", InStock = true });
            AllProducts.Add(new ProductModel { Name = "Наушники Bluetooth", Category = "Аксессуары", Price = 15000, Description = "Глубокие басы, ANC звук.", InStock = true });
            AllProducts.Add(new ProductModel { Name = "Зарядка GaN 65W", Category = "Аксессуары", Price = 3500, Description = "Быстрый заряд 3-х гаджетов.", InStock = true });

            FilteredProducts = CollectionViewSource.GetDefaultView(AllProducts);
            FilteredProducts.Filter = CatalogFilterLogic;

            AddToCartCommand = new RelayCommand(p => ExecuteAddToCart(p));
            RemoveFromCartCommand = new RelayCommand(p => ExecuteRemoveFromCart(p)); // Инициализация удаления
            CheckoutCommand = new RelayCommand(p => ExecuteCheckout(), p => CartItems.Count > 0);
            CartItems.CollectionChanged += (s, e) => UpdateCartTotal();
        }

        public string SelectedCategory { get { return _selectedCategory; } set { _selectedCategory = value; FilteredProducts.Refresh(); OnPropertyChanged(); } }
        public double MaxPrice { get { return _maxPrice; } set { _maxPrice = value; FilteredProducts.Refresh(); OnPropertyChanged(); } }
        public bool OnlyInStock { get { return _onlyInStock; } set { _onlyInStock = value; FilteredProducts.Refresh(); OnPropertyChanged(); } }
        public double CartTotal { get { return _cartTotal; } set { _cartTotal = value; OnPropertyChanged(); } }

        private bool CatalogFilterLogic(object obj)
        {
            ProductModel product = obj as ProductModel;
            if (product == null) return false;
            return (SelectedCategory == "Все категории" || product.Category == SelectedCategory) && (product.Price <= MaxPrice) && (!OnlyInStock || product.InStock);
        }

        private void ExecuteAddToCart(object parameter)
        {
            ProductModel product = parameter as ProductModel;
            if (product == null) return;

            CartItemModel existingItem = CartItems.FirstOrDefault(item => item.Product.Name == product.Name);
            if (existingItem != null) existingItem.Quantity++;
            else CartItems.Add(new CartItemModel { Product = product, Quantity = 1 });
            UpdateCartTotal();
        }

        // ЛОГИКА УДАЛЕНИЯ: если товара > 1, уменьшаем счетчик, если остался один — полностью стираем из списка
        private void ExecuteRemoveFromCart(object parameter)
        {
            CartItemModel cartItem = parameter as CartItemModel;
            if (cartItem == null) return;

            if (cartItem.Quantity > 1)
            {
                cartItem.Quantity--;
            }
            else
            {
                CartItems.Remove(cartItem);
            }
            UpdateCartTotal();
        }

        private void UpdateCartTotal() { CartTotal = CartItems.Sum(item => item.TotalPrice); }
        private void ExecuteCheckout() { MessageBox.Show(string.Format("Заказ оформлен на сумму: {0:C}!", CartTotal), "Успех"); CartItems.Clear(); }

        public event PropertyChangedEventHandler PropertyChanged;
        protected void OnPropertyChanged([CallerMemberName] string name = null)
        {
            if (PropertyChanged != null) PropertyChanged(this, new PropertyChangedEventArgs(name));
        }
    }
    #endregion
}
