import kotlin.math.*

fun parseAge(input: String): Int {
    val age = input.toIntOrNull() ?: return 0
    return if (age >= 0) age else 0
}

fun registerUser(login: String, password: String, age: Int) {
    require(login.isNotBlank()) { "Логин не может быть пустым" }
    require(password.length >= 6) { "Пароль должен быть не короче 6 символов" }
    require(age >= 16) { "Возраст должен быть не меньше 16" }
    println("Пользователь $login успешно зарегистрирован")
}

class BankAccount(var balance: Double) {
    fun withdraw(amount: Double) {
        require(amount > 0) { "Сумма должна быть больше 0" }
        check(balance >= amount) { "Недостаточно средств на счёте" }
        balance -= amount
        println("Снято $amount. Остаток: $balance")
    }
}

fun getProductByIndex(products: List<String>, index: Int): String {
    return products.getOrNull(index) ?: "Товар не найден"
}

sealed class LoginResult {
    data class Success(val login: String) : LoginResult()
    object WrongPassword : LoginResult()
    object UserBlocked : LoginResult()
    data class ServerError(val message: String) : LoginResult()
}

fun login(login: String, password: String): LoginResult {
    return try {
        when {
            login == "blocked" -> LoginResult.UserBlocked
            password != "1234" -> LoginResult.WrongPassword
            else -> LoginResult.Success(login)
        }
    } catch (e: Exception) {
        LoginResult.ServerError(e.message ?: "Неизвестная ошибка")
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

fun validateEmail(email: String): ValidationResult {
    return when {
        email.isBlank() -> ValidationResult.Error("Email не может быть пустым")
        !email.contains("@") -> ValidationResult.Error("Email должен содержать @")
        !email.contains(".") -> ValidationResult.Error("Email должен содержать .")
        else -> ValidationResult.Success
    }
}

data class User(val id: Int, val name: String)

fun loadUser(id: Int): Result<User> {
    return runCatching {
        require(id > 0) { "ID должен быть больше 0" }
        User(id, "User$id")
    }
}

fun createOrder(products: List<String>) {
    check(products.isNotEmpty()) { "Корзина не должна быть пустой" }
    println("Заказ оформлен")
}

fun parsePrice(input: String): Result<Double> {
    return runCatching {
        val price = input.toDouble()
        check(price > 0) { "Цена должна быть больше 0" }
        price
    }
}

enum class OrderStatus {
    CREATED, PAID, DELIVERED, CANCELED
}

fun deliverOrder(status: OrderStatus) {
    check(status == OrderStatus.PAID) { "Доставить можно только заказ со статусом PAID" }
    println("Заказ доставлен")
}

class UserNotFoundException(message: String) : Exception(message)

fun findUser(id: Int): User {
    return if (id == 1) {
        User(1, "Иван")
    } else {
        throw UserNotFoundException("Пользователь с id $id не найден")
    }
}

fun parsePort(portText: String): Int {
    val port = portText.toIntOrNull()
    return if (port != null && port in 1..65535) port else 8080
}

fun setGrade(grade: Int) {
    require(grade in 2..5) { "Оценка должна быть от 2 до 5" }
    println("Оценка сохранена")
}

sealed class AuthResult {
    data class Success(val token: String) : AuthResult()
    object InvalidCredentials : AuthResult()
    object AccountBlocked : AuthResult()
}

class StudentNotFoundException(message: String) : Exception(message)

data class Course(val id: Int, val name: String)
data class Student(val id: Int, val login: String, val password: String)

object OnlineLearningPlatform {
    private val students = mutableListOf<Student>()
    private val courses = listOf(
        Course(1, "Kotlin Basics"),
        Course(2, "Android Development"),
        Course(3, "Backend Development")
    )
    private var currentStudent: Student? = null
    private val enrolledCourses = mutableMapOf<Int, MutableList<Int>>()

    fun registerStudent(login: String, password: String): Boolean {
        return try {
            require(login.isNotBlank()) { "Логин не может быть пустым" }
            require(password.length >= 6) { "Пароль должен быть не короче 6 символов" }
            check(students.none { it.login == login }) { "Пользователь с таким логином уже существует" }
            students.add(Student(students.size + 1, login, password))
            println("Студент $login успешно зарегистрирован")
            true
        } catch (e: Exception) {
            println("Ошибка регистрации: ${e.message}")
            false
        }
    }

    fun loginStudent(login: String, password: String): AuthResult {
        return try {
            require(login.isNotBlank()) { "Логин не может быть пустым" }
            require(password.isNotBlank()) { "Пароль не может быть пустым" }
            
            val student = students.find { it.login == login }
                ?: return AuthResult.InvalidCredentials
            
            if (login == "blocked") return AuthResult.AccountBlocked
            
            if (student.password == password) {
                currentStudent = student
                AuthResult.Success("token_${student.id}")
            } else {
                AuthResult.InvalidCredentials
            }
        } catch (e: Exception) {
            AuthResult.InvalidCredentials
        }
    }

    fun loadCourses(studentId: Int): Result<List<Course>> {
        return runCatching {
            require(studentId > 0) { "ID студента должен быть больше 0" }
            val student = students.find { it.id == studentId }
                ?: throw StudentNotFoundException("Студент с id $studentId не найден")
            println("Курсы загружены для студента ${student.login}")
            courses
        }
    }

    fun enrollToCourse(studentId: Int, courseId: Int) {
        try {
            require(courseId in 1..3) { "Курс с id $courseId не существует" }
            
            val student = students.find { it.id == studentId }
                ?: throw StudentNotFoundException("Студент с id $studentId не найден")
            
            check(currentStudent != null && currentStudent!!.id == studentId) { 
                "Студент должен быть авторизован для записи на курс" 
            }
            
            enrolledCourses.getOrPut(studentId) { mutableListOf() }
            check(enrolledCourses[studentId]?.contains(courseId) != true) { 
                "Студент уже записан на этот курс" 
            }
            
            enrolledCourses[studentId]?.add(courseId)
            println("Студент ${student.login} записан на курс ${courses.find { it.id == courseId }?.name}")
        } catch (e: Exception) {
            println("Ошибка записи на курс: ${e.message}")
        }
    }
}

sealed class OperationResult<out T> {
    data class Success<T>(val data: T) : OperationResult<T>()
    data class BusinessError(val message: String) : OperationResult<Nothing>()
    data class TechnicalError(val exception: Throwable) : OperationResult<Nothing>()
}

data class UserInfo(val id: Int, val name: String, val email: String)

fun createUser(name: String, email: String): OperationResult<UserInfo> {
    return try {
        require(name.isNotBlank()) { "Имя не может быть пустым" }
        require(email.contains("@") && email.contains(".")) { "Неверный формат email" }
        
        val userId = (1..1000).random()
        OperationResult.Success(UserInfo(userId, name, email))
    } catch (e: IllegalArgumentException) {
        OperationResult.BusinessError(e.message ?: "Ошибка валидации")
    } catch (e: Exception) {
        OperationResult.TechnicalError(e)
    }
}

fun makePayment(userId: Int, amount: Double): OperationResult<String> {
    return try {
        require(userId > 0) { "Неверный ID пользователя" }
        require(amount > 0) { "Сумма платежа должна быть больше 0" }
        
        if (amount > 10000) {
            return OperationResult.BusinessError("Превышен лимит платежа: $amount > 10000")
        }
        
        OperationResult.Success("Платеж на сумму $amount успешно выполнен")
    } catch (e: IllegalArgumentException) {
        OperationResult.BusinessError(e.message ?: "Ошибка валидации")
    } catch (e: Exception) {
        OperationResult.TechnicalError(e)
    }
}

fun loadData(url: String): OperationResult<String> {
    return try {
        require(url.startsWith("http")) { "Неверный формат URL" }
        
        if (url.contains("error")) {
            throw RuntimeException("Ошибка загрузки данных с сервера")
        }
        
        OperationResult.Success("Данные с $url успешно загружены")
    } catch (e: IllegalArgumentException) {
        OperationResult.BusinessError(e.message ?: "Ошибка валидации URL")
    } catch (e: Exception) {
        OperationResult.TechnicalError(e)
    }
}

fun main() {
    println("Задание 1")
    println(parseAge("25"))    // 25
    println(parseAge("-5"))    // 0
    println(parseAge("abc"))   // 0
    
    println("\nЗадание 2")
    registerUser("john_doe", "pass123", 20)
    
    println("\nЗадание 3")
    val account = BankAccount(1000.0)
    account.withdraw(500.0)
    
    println("\nЗадание 4")
    val products = listOf("Ноутбук", "Телефон", "Планшет")
    println(getProductByIndex(products, 1))  // Телефон
    println(getProductByIndex(products, 5))  // Товар не найден
    
    println("\nЗадание 5 ")
    when (val result = login("user", "1234")) {
        is LoginResult.Success -> println("Успешный вход: ${result.login}")
        LoginResult.WrongPassword -> println("Неверный пароль")
        LoginResult.UserBlocked -> println("Пользователь заблокирован")
        is LoginResult.ServerError -> println("Ошибка сервера: ${result.message}")
    }
    
    println("\nЗадание 6 ")
    when (val result = validateEmail("test@example.com")) {
        ValidationResult.Success -> println("Email валидный")
        is ValidationResult.Error -> println("Ошибка: ${result.message}")
    }
    
    println("\nЗадание 7 ")
    loadUser(1)
        .onSuccess { println("Пользователь загружен: ${it.name}") }
        .onFailure { println("Ошибка загрузки: ${it.message}") }
    
    println("\nЗадание 8 ")
    createOrder(listOf("Товар1", "Товар2"))
    
    println("\nЗадание 9 ")
    val priceResult = parsePrice("99.99")
    println(priceResult.getOrElse { 0.0 })
    
    println("\nЗадание 10 ")
    deliverOrder(OrderStatus.PAID)
    
    println("\nЗадание 11 ")
    try {
        println(findUser(1))
        println(findUser(2))
    } catch (e: UserNotFoundException) {
        println("Ошибка: ${e.message}")
    }
    
    println("\nЗадание 12 ")
    println(parsePort("8080"))
    println(parsePort("99999"))
    println(parsePort("abc"))
    
    println("\nЗадание 13 ")
    setGrade(4)
    
    println("\nЗадание 14 ")
    OnlineLearningPlatform.registerStudent("alice", "pass123")
    when (val auth = OnlineLearningPlatform.loginStudent("alice", "pass123")) {
        is AuthResult.Success -> {
            println("Авторизация успешна: ${auth.token}")
            OnlineLearningPlatform.loadCourses(1)
                .onSuccess { courses -> println("Загружено ${courses.size} курсов") }
            OnlineLearningPlatform.enrollToCourse(1, 1)
        }
        AuthResult.InvalidCredentials -> println("Неверные учетные данные")
        AuthResult.AccountBlocked -> println("Аккаунт заблокирован")
    }
    
    println("\nЗадание повышенной сложности ")
    when (val userResult = createUser("Иван", "ivan@example.com")) {
        is OperationResult.Success -> println("Пользователь создан: ${userResult.data}")
        is OperationResult.BusinessError -> println("Бизнес-ошибка: ${userResult.message}")
        is OperationResult.TechnicalError -> println("Техническая ошибка: ${userResult.exception.message}")
    }
    
    when (val paymentResult = makePayment(1, 500.0)) {
        is OperationResult.Success -> println(paymentResult.data)
        is OperationResult.BusinessError -> println("Бизнес-ошибка: ${paymentResult.message}")
        is OperationResult.TechnicalError -> println("Техническая ошибка: ${paymentResult.exception.message}")
    }
    
    when (val loadResult = loadData("https://api.example.com")) {
        is OperationResult.Success -> println(loadResult.data)
        is OperationResult.BusinessError -> println("Бизнес-ошибка: ${loadResult.message}")
        is OperationResult.TechnicalError -> println("Техническая ошибка: ${loadResult.exception.message}")
    }
}
