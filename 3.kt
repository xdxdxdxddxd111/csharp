// Задание 1
class Box<T>(private var value: T) {
    fun getValue(): T = value
    fun setValue(newValue: T) { value = newValue }
}

data class User(val name: String, val age: Int)

// Задание 2
class Catalog<T> {
    private val items = mutableListOf<T>()
    
    fun add(item: T) { items.add(item) }
    fun remove(item: T) { items.remove(item) }
    fun getAll(): List<T> = items.toList()
    fun find(predicate: (T) -> Boolean): T? = items.find(predicate)
    fun count(): Int = items.size
}

data class Product(val name: String, val price: Double)
data class Course(val title: String, val hours: Int)

// Задание 3
interface Repository<T> {
    fun add(item: T)
    fun remove(item: T)
    fun update(item: T, newItem: T)
    fun getAll(): List<T>
    fun find(predicate: (T) -> Boolean): T?
}

class MemoryRepository<T> : Repository<T> {
    private val items = mutableListOf<T>()
    
    override fun add(item: T) { items.add(item) }
    override fun remove(item: T) { items.remove(item) }
    override fun update(item: T, newItem: T) {
        val index = items.indexOf(item)
        if (index != -1) items[index] = newItem
    }
    override fun getAll(): List<T> = items.toList()
    override fun find(predicate: (T) -> Boolean): T? = items.find(predicate)
}

// Задание 4
fun <T> findFirst(items: List<T>, condition: (T) -> Boolean): T? = items.find(condition)

// Задание 5
fun <T> printCollection(items: List<T>) {
    items.forEach { println(it) }
}

// Задание 6
fun <T : Number> averageValue(items: List<T>): Double {
    return items.map { it.toDouble() }.average()
}

fun <T : Number> maxValue(items: List<T>): Double = items.maxOfOrNull { it.toDouble() } ?: 0.0
fun <T : Number> minValue(items: List<T>): Double = items.minOfOrNull { it.toDouble() } ?: 0.0

// Задание 7
fun <T> List<T>.printAll() = forEach { println(it) }
fun <T> List<T>.secondOrNull(): T? = if (size >= 2) this[1] else null
fun <T> List<T>.lastOrDefault(defaultValue: T): T = lastOrNull() ?: defaultValue

// Задание 8
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val message: String) : Result<T>()
    data class Loading<T>(val progress: Int = 0) : Result<T>()
}

fun getUser(): Result<User> = Result.Success(User("Иван", 25))
fun getProducts(): Result<List<Product>> = Result.Success(listOf(Product("Ноутбук", 50000.0), Product("Мышь", 1000.0)))

// Задание 9
class Cache<T> {
    private var item: T? = null
    
    fun save(data: T) { item = data }
    fun get(): T? = item
    fun clear() { item = null }
    fun isEmpty(): Boolean = item == null
}

// Задание 10
data class Student(val name: String, val grades: List<Int>)

fun <T : Number> calculateAverage(values: List<T>): Double {
    return values.map { it.toDouble() }.average()
}

// Задание 11
fun <T> countMatching(items: List<T>, condition: (T) -> Boolean): Int = items.count(condition)

// Задание 12
data class PairBox<K, V>(val key: K, val value: V)

// Задание 13
fun printSize(items: List<*>) {
    println("Размер списка: ${items.size}")
    if (items.isNotEmpty()) {
        println("Тип первого элемента: ${items[0]?.javaClass?.simpleName ?: "null"}")
    }
}

// Задание 14
interface Printable {
    fun printInfo()
}

fun <T : Printable> printObject(item: T) = item.printInfo()

// Реализация Printable для моделей
fun User.printInfo() = println("User: $name, $age лет")
fun Product.printInfo() = println("Product: $name, ${price} руб")
fun Course.printInfo() = println("Course: $title, $hours часов")

// Задание 15
data class Teacher(val name: String, val subject: String)

class EducationalPortal {
    val studentRepo = MemoryRepository<Student>()
    val courseRepo = MemoryRepository<Course>()
    val teacherRepo = MemoryRepository<Teacher>()
    val studentCache = Cache<List<Student>>()
    val courseCache = Cache<List<Course>>()
    
    fun calculateAverageGrade(grades: List<Int>) = calculateAverage(grades)
    
    fun findStudentsByAge(age: Int) = studentRepo.getAll().filter { it.grades.isNotEmpty() }
}

// Задание повышенной сложности
class DataManager<T> {
    private val items = mutableListOf<T>()
    
    fun add(item: T) { items.add(item) }
    fun remove(item: T) { items.remove(item) }
    fun find(predicate: (T) -> Boolean): T? = items.find(predicate)
    fun sort(comparator: Comparator<T>) { items.sortWith(comparator) }
    fun filter(predicate: (T) -> Boolean): List<T> = items.filter(predicate)
    fun count(): Int = items.size
    fun getAll(): List<T> = items.toList()
}

// ГЛАВНАЯ ФУНКЦИЯ
fun main() {
    println("Задание 1")
    val boxInt = Box(100)
    val boxString = Box("Привет")
    val boxDouble = Box(3.14)
    val boxUser = Box(User("Анна", 30))
    
    println("Int: ${boxInt.getValue()}")
    println("String: ${boxString.getValue()}")
    println("Double: ${boxDouble.getValue()}")
    println("User: ${boxUser.getValue()}")
    boxUser.setValue(User("Мария", 28))
    println("Измененный User: ${boxUser.getValue()}")
    
    println("\nЗадание 2")
    val userCatalog = Catalog<User>()
    userCatalog.add(User("Алексей", 20))
    userCatalog.add(User("Ольга", 35))
    println("Каталог пользователей: ${userCatalog.getAll()}")
    println("Найден: ${userCatalog.find { it.name == "Ольга" }}")
    println("Количество: ${userCatalog.count()}")
    
    val productCatalog = Catalog<Product>()
    productCatalog.add(Product("Телефон", 30000.0))
    println("Каталог товаров: ${productCatalog.getAll()}")
    
    val courseCatalog = Catalog<Course>()
    courseCatalog.add(Course("Kotlin", 40))
    println("Каталог курсов: ${courseCatalog.getAll()}")
    
    println("\nЗадание 3")
    val userRepo = MemoryRepository<User>()
    val user1 = User("Дмитрий", 22)
    userRepo.add(user1)
    println("Все пользователи: ${userRepo.getAll()}")
    
    val productRepo = MemoryRepository<Product>()
    productRepo.add(Product("Книга", 500.0))
    println("Все товары: ${productRepo.getAll()}")
    
    val courseRepo = MemoryRepository<Course>()
    courseRepo.add(Course("Java", 60))
    println("Все курсы: ${courseRepo.getAll()}")
    
    println("\nЗадание 4")
    val users = listOf(User("Антон", 19), User("Елена", 42))
    println("Поиск по имени Антон: ${findFirst(users) { it.name == "Антон" }}")
    
    val products = listOf(Product("Хлеб", 50.0), Product("Молоко", 80.0))
    println("Поиск по цене > 60: ${findFirst(products) { it.price > 60 }}")
    
    val courses = listOf(Course("Python", 30), Course("C++", 50))
    println("Поиск по названию Python: ${findFirst(courses) { it.title == "Python" }}")
    
    println("\nЗадание 5")
    printCollection(listOf(1, 2, 3))
    printCollection(listOf("A", "B", "C"))
    printCollection(users)
    printCollection(products)
    
    println("\nЗадание 6")
    val intList = listOf(10, 20, 30)
    val doubleList = listOf(1.5, 2.5, 3.5)
    val floatList = listOf(1.1f, 2.2f, 3.3f)
    
    println("Среднее Int: ${averageValue(intList)}")
    println("Среднее Double: ${averageValue(doubleList)}")
    println("Среднее Float: ${averageValue(floatList)}")
    println("Макс Int: ${maxValue(intList)}, Мин Int: ${minValue(intList)}")
    
    println("\nЗадание 7")
    val testList = listOf(1, 2, 3, 4, 5)
    testList.printAll()
    println("Второй элемент: ${testList.secondOrNull()}")
    println("Последний или default: ${emptyList<String>().lastOrDefault("default")}")
    
    println("\nЗадание 8")
    when (val result = getUser()) {
        is Result.Success -> println("Успех: ${result.data}")
        is Result.Error -> println("Ошибка: ${result.message}")
        is Result.Loading -> println("Загрузка: ${result.progress}%")
    }
    
    when (val result = getProducts()) {
        is Result.Success -> println("Товары: ${result.data}")
        is Result.Error -> println("Ошибка: ${result.message}")
        is Result.Loading -> println("Загрузка: ${result.progress}%")
    }
    
    println("\nЗадание 9")
    val userCache = Cache<User>()
    userCache.save(User("Кирилл", 27))
    println("Кеш пользователя: ${userCache.get()}")
    println("Пуст?: ${userCache.isEmpty()}")
    userCache.clear()
    println("После очистки: ${userCache.get()}")
    
    val productListCache = Cache<List<Product>>()
    productListCache.save(listOf(Product("Стул", 2000.0)))
    println("Кеш списка товаров: ${productListCache.get()}")
    
    println("\nЗадание 10: Система оценок")
    val students = listOf(
        Student("Иван", listOf(5, 4, 5, 3)),
        Student("Мария", listOf(5, 5, 5, 4))
    )
    students.forEach { student ->
        println("${student.name}: Средний=${calculateAverage(student.grades)}, " +
                "Макс=${student.grades.maxOrNull()}, Мин=${student.grades.minOrNull()}")
    }
    
    println("\nЗадание 11")
    println("Совершеннолетних: ${countMatching(users) { it.age >= 18 }}")
    println("Дорогие товары (>100): ${countMatching(products) { it.price > 100 }}")
    println("Активные курсы: ${countMatching(courses) { true }}")
    
    println("\nЗадание 12")
    val userPair = PairBox(1, User("Егор", 33))
    val pricePair = PairBox("Телефон", 30000.0)
    val coursePair = PairBox("K101", Course("Kotlin", 40))
    println("User: id=${userPair.key} -> ${userPair.value}")
    println("Price: ${pricePair.key}=${pricePair.value}")
    println("Course: ${coursePair.key}=${coursePair.value}")
    
    println("\nЗадание 13")
    printSize(listOf("A", "B", "C"))
    printSize(listOf(1, 2, 3, 4))
    printSize(users)
    
    println("\nЗадание 14")
    User("Максим", 29).printInfo()
    Product("Монитор", 15000.0).printInfo()
    Course("Web разработка", 80).printInfo()
    
    println("\nЗадание 15")
    val portal = EducationalPortal()
    portal.studentRepo.add(Student("Анна", listOf(5, 4, 5)))
    portal.courseRepo.add(Course("Kotlin", 40))
    portal.teacherRepo.add(Teacher("Ирина", "Программирование"))
    println("Студенты: ${portal.studentRepo.getAll()}")
    println("Курсы: ${portal.courseRepo.getAll()}")
    println("Преподаватели: ${portal.teacherRepo.getAll()}")
    
    println("\nЗадание повышенной сложности: DataManager")
    val userManager = DataManager<User>()
    userManager.add(User("Владимир", 35))
    userManager.add(User("Александр", 28))
    userManager.add(User("Борис", 42))
    println("Все пользователи: ${userManager.getAll()}")
    println("Фильтр по возрасту >30: ${userManager.filter { it.age > 30 }}")
    println("Количество: ${userManager.count()}")
    
    val productManager = DataManager<Product>()
    productManager.add(Product("Мышь", 1500.0))
    productManager.add(Product("Клавиатура", 2500.0))
    println("Товары: ${productManager.getAll()}")
    
    val courseManager = DataManager<Course>()
    courseManager.add(Course("SQL", 25))
    courseManager.add(Course("Git", 15))
    println("Курсы: ${courseManager.getAll()}")
}
