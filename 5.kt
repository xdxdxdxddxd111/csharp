import kotlin.properties.Delegates
import kotlin.math.*

// Data class для использования 
data class UserData(val id: Int, val name: String, val role: String)
data class ProductData(val name: String, val price: Double, val category: String)
data class CourseData(val id: Int, val name: String, val price: Double, val maxSeats: Int)

// Задание 1 
class DatabaseConnection {
    init {
        println("Подключение создано")
    }
}

// Задание 2 
class Report {
    init {
        println("Отчёт сформирован")
    }
}

// Задание 3 
class Product {
    var name: String
    var price: Double by Delegates.observable(0.0) { _, oldValue, newValue ->
        println("Цена товара '$name' изменена: Старая цена: $oldValue, Новая цена: $newValue")
    }
    
    constructor(name: String, initialPrice: Double) {
        this.name = name
        this.price = initialPrice
    }
}

// Задание 4 
class Employee {
    var name: String
    var age: Int by Delegates.vetoable(0) { _, _, newValue ->
        when {
            newValue < 18 -> {
                println("Ошибка: Возраст $newValue меньше 18 лет - изменение отклонено")
                false
            }
            newValue > 80 -> {
                println("Ошибка: Возраст $newValue больше 80 лет - изменение отклонено")
                false
            }
            else -> {
                println("Возраст сотрудника '$name' успешно изменён на $newValue")
                true
            }
        }
    }
    
    constructor(name: String, initialAge: Int) {
        this.name = name
        this.age = initialAge
    }
}

// Задание 5 
class Discount {
    var percent: Int by Delegates.vetoable(0) { _, oldValue, newValue ->
        when {
            newValue < 0 -> {
                println("Ошибка: Скидка $newValue% меньше 0% - изменение отклонено (текущая: $oldValue%)")
                false
            }
            newValue > 90 -> {
                println("Ошибка: Скидка $newValue% больше 90% - изменение отклонено (текущая: $oldValue%)")
                false
            }
            else -> {
                println("Скидка успешно изменена: $oldValue% -> $newValue%")
                true
            }
        }
    }
}

// Задание 6 
object AppSettings {
    var appName: String = "MyApp"
    var version: String = "1.0.0"
    var theme: String = "Light"
    
    fun showSettings(source: String) {
        println("[$source] Настройки: appName=$appName, version=$version, theme=$theme")
    }
}

// Задание 7 
object UserStatistics {
    private var userCount = 0
    
    fun registerUser() {
        userCount++
        println("Пользователь зарегистрирован. Всего: $userCount")
    }
    
    fun getTotalUsers(): Int = userCount
}

// Задание 8 
class User(val name: String, val role: String) {
    companion object {
        fun createGuest(): User {
            println("Создан гость")
            return User("Гость", "guest")
        }
        
        fun createAdmin(): User {
            println("Создан администратор")
            return User("Администратор", "admin")
        }
    }
    
    override fun toString(): String = "User(name='$name', role='$role')"
}

// Задание 9 
class ProductDemo(val name: String, val price: Double, val brand: String) {
    companion object {
        fun demoLaptop(): ProductDemo = ProductDemo("MacBook Pro", 1999.99, "Apple")
        fun demoPhone(): ProductDemo = ProductDemo("iPhone 15", 999.99, "Apple")
        fun demoTablet(): ProductDemo = ProductDemo("iPad Pro", 799.99, "Apple")
    }
    
    override fun toString(): String = "Product(name='$name', price=$price, brand='$brand')"
}

// Задание 10 
val settings = object {
    val theme: String = "Dark"
    val fontSize: Int = 14
    val language: String = "Russian"
    
    fun show() {
        println("Анонимный объект: theme='$theme', fontSize=$fontSize, language='$language'")
    }
}

// Задание 11 
interface Logger {
    fun log(message: String)
}

class ConsoleLogger : Logger {
    override fun log(message: String) {
        println("[ConsoleLogger] $message")
    }
}

class UserService(private val name: String, logger: Logger) : Logger by logger {
    fun performAction() {
        log("Пользователь $name выполняет действие")
        println("Действие выполнено")
    }
}

// Задание 12 
interface Notifier {
    fun notify(message: String)
}

class EmailNotifier : Notifier {
    override fun notify(message: String) {
        println("[EmailNotifier] Отправлено email: $message")
    }
}

class SmsNotifier : Notifier {
    override fun notify(message: String) {
        println("[SmsNotifier] Отправлено SMS: $message")
    }
}

class OrderService(orderId: String, notifier: Notifier) : Notifier by notifier {
    fun placeOrder() {
        println("Заказ $orderId оформлен")
        notify("Заказ $orderId успешно создан")
    }
}

// Задание 13 
interface AdvancedLogger {
    fun log(message: String)
}

class ConsoleAdvancedLogger : AdvancedLogger {
    override fun log(message: String) {
        println("[ConsoleLogger] $message")
    }
}

class FileLogger : AdvancedLogger {
    override fun log(message: String) {
        println("[FileLogger] Запись в файл: $message")
    }
}

class DatabaseLogger : AdvancedLogger {
    override fun log(message: String) {
        println("[DatabaseLogger] Запись в БД: $message")
    }
}

class ProductService(private val productName: String, logger: AdvancedLogger) : AdvancedLogger by logger {
    fun saveProduct() {
        log("Продукт '$productName' сохранён")
    }
}

// Задание 14 
object ApiClient {
    val connection by lazy {
        println("HTTP клиент создан")
        "HTTP-соединение"
    }
    
    fun request() {
        println("Выполняется запрос через $connection")
    }
}

// Задание 15 
interface PortalLogger {
    fun log(message: String)
}

interface PortalNotifier {
    fun send(message: String)
}

class PortalConsoleLogger : PortalLogger {
    override fun log(message: String) {
        println("[PortalLog] $message")
    }
}

class PortalEmailNotifier : PortalNotifier {
    override fun send(message: String) {
        println("[PortalNotify] $message")
    }
}

object PortalSettings {
    var portalName: String = "Образовательный портал"
    var version: String = "2.0.0"
}

class DatabasePortalConnection {
    init {
        println("Подключение к БД учебного портала создано")
    }
}

data class Course(val id: Int, val name: String, var price: Double, var availableSeats: Int) {
    var priceObserver by Delegates.observable(price) { _, oldValue, newValue ->
        println("Курс '$name': цена изменена с $oldValue на $newValue")
    }
    
    var seats by Delegates.vetoable(availableSeats) { _, _, newValue ->
        when {
            newValue < 1 -> {
                println("Ошибка: количество мест $newValue меньше 1 - изменение отклонено")
                false
            }
            newValue > 100 -> {
                println("Ошибка: количество мест $newValue больше 100 - изменение отклонено")
                false
            }
            else -> {
                println("Курс '$name': количество мест изменено на $newValue")
                true
            }
        }
    }
    
    companion object {
        fun demo(): Course = Course(1, "Kotlin Programming", 299.99, 30)
    }
}

data class PortalStudent(val name: String, val email: String, val type: String) {
    companion object {
        fun guest(): PortalStudent = PortalStudent("Гость", "guest@portal.com", "guest")
    }
}

data class PortalTeacher(val name: String, val subject: String, val role: String) {
    companion object {
        fun admin(): PortalTeacher = PortalTeacher("Администратор", "Все предметы", "admin")
    }
}

class EducationalPortal(
    private val logger: PortalLogger,
    private val notifier: PortalNotifier
) : PortalLogger by logger, PortalNotifier by notifier {
    private val database by lazy {
        DatabasePortalConnection()
    }
    
    private val courses = mutableListOf<Course>()
    private val students = mutableListOf<PortalStudent>()
    
    fun addCourse(course: Course) {
        courses.add(course)
        log("Добавлен курс: ${course.name}")
        send("Новый курс добавлен: ${course.name}")
    }
    
    fun registerStudent(student: PortalStudent) {
        students.add(student)
        log("Зарегистрирован студент: ${student.name}")
        database // обращение к lazy свойству
    }
    
    fun showStatistics() {
        println("\n=== Статистика портала ===")
        println("Курсов: ${courses.size}")
        println("Студентов: ${students.size}")
        courses.forEach { course ->
            println("  - ${course.name}: цена=${course.price}, мест=${course.availableSeats}")
        }
    }
}

// Задание повышенной сложности 
interface ServerLogger {
    fun logEvent(event: String)
}

class MonitoringLogger : ServerLogger {
    override fun logEvent(event: String) {
        println("[Мониторинг] $event")
    }
}

data class ServerStatus(val serverId: Int, var status: String, var cpuLoad: Int, var memoryLoad: Int)

class Server(private val id: Int, initialCpuLoad: Int, initialMemoryLoad: Int, logger: ServerLogger) : ServerLogger by logger {
    var status: String by Delegates.observable("Stopped") { _, oldValue, newValue ->
        logEvent("Сервер $id: статус изменён с '$oldValue' на '$newValue'")
    }
    
    var cpuLoad: Int by Delegates.vetoable(initialCpuLoad) { _, oldValue, newValue ->
        when {
            newValue < 0 -> {
                logEvent("Ошибка: CPU нагрузка $newValue% меньше 0 - отклонено")
                false
            }
            newValue > 100 -> {
                logEvent("Ошибка: CPU нагрузка $newValue% больше 100 - отклонено")
                false
            }
            else -> {
                logEvent("Сервер $id: CPU нагрузка изменена с $oldValue% на $newValue%")
                true
            }
        }
    }
    
    var memoryLoad: Int by Delegates.vetoable(initialMemoryLoad) { _, oldValue, newValue ->
        when {
            newValue < 0 -> {
                logEvent("Ошибка: Memory нагрузка $newValue% меньше 0 - отклонено")
                false
            }
            newValue > 100 -> {
                logEvent("Ошибка: Memory нагрузка $newValue% больше 100 - отклонено")
                false
            }
            else -> {
                logEvent("Сервер $id: Memory нагрузка изменена с $oldValue% на $newValue%")
                true
            }
        }
    }
    
    init {
        status = "Running"
    }
}

object ServerStatistics {
    private val servers = mutableListOf<Server>()
    
    fun registerServer(server: Server) {
        servers.add(server)
        println("Сервер ${server::class.java} зарегистрирован. Всего серверов: ${servers.size}")
    }
    
    fun getTotalServers(): Int = servers.size
    
    fun showSummary() {
        println("\n=== Общая статистика серверов ===")
        println("Всего серверов: ${servers.size}")
    }
}

// Главная функция 
fun main() {
    println("Задание 1 и 2")
    val database: DatabaseConnection by lazy { DatabaseConnection() }
    val report: Report by lazy { Report() }
    
    println("Программа запущена без обращения к database и report")
    println("Обращаемся к database:")
    database.toString()
    println("Обращаемся к report:")
    report.toString()
    
    println("\nЗадание 3")
    val product = Product("Ноутбук", 50000.0)
    product.price = 55000.0
    product.price = 60000.0
    product.price = 52000.0
    
    println("\nЗадание 4")
    val employee = Employee("Иван", 25)
    employee.age = 30
    employee.age = 17
    employee.age = 85
    employee.age = 40
    
    println("\nЗадание 5")
    val discount = Discount()
    discount.percent = 50
    discount.percent = 95
    discount.percent = -10
    discount.percent = 30
    
    println("\nЗадание 6")
    AppSettings.showSettings("Первая часть")
    AppSettings.appName = "SuperApp"
    AppSettings.version = "2.0.0"
    AppSettings.showSettings("Вторая часть")
    
    println("\nЗадание 7")
    UserStatistics.registerUser()
    UserStatistics.registerUser()
    UserStatistics.registerUser()
    println("Итого пользователей: ${UserStatistics.getTotalUsers()}")
    
    println("\nЗадание 8")
    val guest = User.createGuest()
    val admin = User.createAdmin()
    println(guest)
    println(admin)
    
    println("\nЗадание 9")
    val demoProducts = listOf(
        ProductDemo.demoLaptop(),
        ProductDemo.demoPhone(),
        ProductDemo.demoTablet()
    )
    demoProducts.forEach { println(it) }
    
    println("\nЗадание 10")
    settings.show()
    
    println("\nЗадание 11")
    val userService = UserService("Алексей", ConsoleLogger())
    userService.performAction()
    
    println("\nЗадание 12")
    val emailOrder = OrderService("ORDER-001", EmailNotifier())
    val smsOrder = OrderService("ORDER-002", SmsNotifier())
    emailOrder.placeOrder()
    smsOrder.placeOrder()
    
    println("\nЗадание 13")
    val productServiceConsole = ProductService("Книга", ConsoleAdvancedLogger())
    productServiceConsole.saveProduct()
    
    val productServiceFile = ProductService("Книга", FileLogger())
    productServiceFile.saveProduct()
    
    val productServiceDB = ProductService("Книга", DatabaseLogger())
    productServiceDB.saveProduct()
    
    println("\nЗадание 14: ")
    println("ApiClient создан, но connection ещё нет")
    ApiClient.request()
    ApiClient.request()
    ApiClient.request()
    
    println("\nЗадание 15: Комплексное задание ")
    val portal = EducationalPortal(PortalConsoleLogger(), PortalEmailNotifier())
    
    val course = Course.demo()
    course.priceObserver = course.price
    course.price = 349.99
    
    course.seats = course.availableSeats
    course.seats = 50
    course.seats = 150
    course.seats = 0
    
    portal.addCourse(course)
    portal.registerStudent(PortalStudent.guest())
    portal.registerStudent(PortalStudent("Мария", "maria@mail.com", "student"))
    portal.showStatistics()
    
    println("\nЗадание повышенной сложности ")
    val monitorLogger = MonitoringLogger()
    val server1 = Server(1, 45, 60, monitorLogger)
    val server2 = Server(2, 30, 40, monitorLogger)
    
    server1.cpuLoad = 75
    server1.cpuLoad = 120
    server1.cpuLoad = 50
    
    server1.memoryLoad = 80
    server1.memoryLoad = -5
    
    server1.status = "Maintenance"
    
    ServerStatistics.registerServer(server1)
    ServerStatistics.registerServer(server2)
    ServerStatistics.showSummary()
    
    // Дополнительная проверка коллекций
    println("\nРабота с коллекциями ")
    val users = listOf(
        UserData(1, "Анна", "студент"),
        UserData(2, "Петр", "преподаватель"),
        UserData(3, "Мария", "админ")
    )
    users.forEach { println(it) }
    
    val prices = listOf(100.0, 200.0, 300.0)
    val doubledPrices = prices.map { it * 2 }
    println("Исходные цены: $prices")
    println("Удвоенные цены: $doubledPrices")
}
