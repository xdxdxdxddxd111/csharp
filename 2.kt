fun User.fullName(): String = "$firstName $lastName"

fun String?.orNotSpecified(): String = this ?: "Не указано"

fun Int.isAdult(): Boolean = this >= 18

fun User.getShortInfo(): String = "${firstName} ${lastName} ($age лет)"

fun String.normalizeName(): String = this.trim().split(" ").joinToString(" ") { 
    it.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } 
}

fun Movie.toCardText(): String = "${title} - ${rating.toRatingText()} | ${genre} | ${duration} мин."

fun Double.toRatingText(): String = "$this / 10"

fun List<Movie>.topRated(): List<Movie> = this.filter { it.rating > 8.0 }

fun List<Movie>.shortMovies(): List<Movie> = this.filter { it.duration < 100 }

fun String.shorten(maxLength: Int = 20): String = 
    if (this.length > maxLength) this.take(maxLength) + "..." else this

fun String.isEmailValid(): Boolean = 
    this.contains("@") && this.contains(".") && this.length > 5

fun Double.toPriceText(): String = String.format("%,.2f руб.", this)

fun List<Int>.averageValue(): Double = 
    if (this.isEmpty()) 0.0 else this.sum().toDouble() / this.size

fun Int.square(): Int = this * this

fun Double?.toPriceText(): String = this?.let { String.format("%,.2f руб.", it) } ?: "Цена не указана"

fun Product.toCardText(): String = "${name} - ${price.toPriceText()}  Категория: $category"

fun List<Product>.expensiveProducts(): List<Product> = this.filter { (it.price ?: 0.0) > 10000 }

fun List<Product>.averagePrice(): Double {
    val pricesWithValue = this.filter { it.price != null && it.price!! > 0 }
    return if (pricesWithValue.isEmpty()) 0.0 
    else pricesWithValue.sumOf { it.price!! } / pricesWithValue.size
}

fun List<Movie>.averageRating(): Double {
    return if (this.isEmpty()) 0.0 else this.sumOf { it.rating } / this.size
}

fun List<User>.averageAge(): Double {
    return if (this.isEmpty()) 0.0 else this.sumOf { it.age } / this.size
}

data class User(
    var firstName: String,
    var lastName: String,
    var age: Int,
    var email: String?,
    var phone: String?
)

data class Movie(
    val title: String,
    val rating: Double,
    val genre: String,
    val duration: Int
)

data class Student(
    val name: String,
    val email: String?,
    val group: String?,
    val phone: String?
)

data class Product(
    val name: String,
    val price: Double?,
    val category: String
)

// Задание 1
fun createUsers(): List<User> {
    val user1 = User("иван", "петров", 25, "ivan@mail.ru", "+79123456789")
    val user2 = User("мария", "сидорова", 17, null, "+79998887766")
    val user3 = User("петр", "ИВАНОВ", 30, "petr@mail.ru", null)
    val user4 = User("елена", "кузнецова", 16, null, null)
    val user5 = User("алексей", "смирнов", 42, "alex@mail.ru", "+79223334455")
    
    user1.apply {
        firstName = firstName.normalizeName()
        lastName = lastName.normalizeName()
    }
    user2.apply {
        firstName = firstName.normalizeName()
        lastName = lastName.normalizeName()
    }
    user3.apply {
        firstName = firstName.normalizeName()
        lastName = lastName.normalizeName()
    }
    user4.apply {
        firstName = firstName.normalizeName()
        lastName = lastName.normalizeName()
    }
    user5.apply {
        firstName = firstName.normalizeName()
        lastName = lastName.normalizeName()
    }
    
    return listOf(user1, user2, user3, user4, user5)
}

// Задание 2
fun createMovies(): List<Movie> {
    return listOf(
        Movie("Побег из Шоушенка", 9.3, "Драма", 142),
        Movie("Крестный отец", 9.2, "Криминал", 175),
        Movie("Темный рыцарь", 9.0, "Боевик", 152),
        Movie("Криминальное чтиво", 8.9, "Криминал", 154),
        Movie("Хороший, плохой, злой", 8.8, "Вестерн", 178),
        Movie("Бойцовский клуб", 8.8, "Триллер", 139),
        Movie("Начало", 8.7, "Фантастика", 148),
        Movie("Король Лев", 8.5, "Мультфильм", 88),
        Movie("Шрэк", 7.9, "Мультфильм", 90),
        Movie("Матрица", 8.7, "Фантастика", 136)
    )
}

// Задание 3
fun createUserWithApply(): User {
    return User("", "", 0, null, null).apply {
        firstName = "Дмитрий"
        lastName = "Козлов"
        age = 28
        email = "dima@mail.ru"
        phone = "+79005556677"
    }.also {
        println("Пользователь ${it.fullName()} успешно создан")
    }
}

fun createUsersWithApply(): List<User> {
    val users = mutableListOf<User>()
    
    users.add(User("", "", 0, null, null).apply {
        firstName = "Ольга"
        lastName = "Морозова"
        age = 32
        email = "olga@mail.ru"
        phone = null
    }.also { println("Пользователь ${it.fullName()} успешно создан") })
    
    users.add(User("", "", 0, null, null).apply {
        firstName = "Сергей"
        lastName = "Николаев"
        age = 45
        email = null
        phone = "+79887776655"
    }.also { println("Пользователь ${it.fullName()} успешно создан") })
    
    users.add(User("", "", 0, null, null).apply {
        firstName = "Анна"
        lastName = "Федорова"
        age = 23
        email = "anna@mail.ru"
        phone = "+79554443322"
    }.also { println("Пользователь ${it.fullName()} успешно создан") })
    
    users.add(User("", "", 0, null, null).apply {
        firstName = "Владимир"
        lastName = "Егоров"
        age = 38
        email = "vlad@mail.ru"
        phone = "+79334445566"
    }.also { println("Пользователь ${it.fullName()} успешно создан") })
    
    users.add(User("", "", 0, null, null).apply {
        firstName = "Татьяна"
        lastName = "Павлова"
        age = 29
        email = null
        phone = null
    }.also { println("Пользователь ${it.fullName()} успешно создан") })
    
    return users
}

// Задание 4
fun findUserByEmail(users: List<User>, email: String) {
    println("\nПоиск пользователя по email: $email")
    users.find { it.email == email }.let { foundUser ->
        if (foundUser != null) {
            println("Пользователь найден: ${foundUser.fullName()} (${foundUser.age} лет)")
        } else {
            println("Пользователь не найден")
        }
    }
}

// Задание 5
fun generateMoviesReport(movies: List<Movie>) {
    val report = movies.run {
        """
        ОТЧЁТ ПО КАТАЛОГУ ФИЛЬМОВ
        Количество фильмов: ${this.size}
        Средний рейтинг: ${String.format("%.2f", this.averageRating())}
        Максимальный рейтинг: ${this.maxOfOrNull { it.rating } ?: 0}
        Минимальный рейтинг: ${this.minOfOrNull { it.rating } ?: 0}
        """
    }
    println(report)
}

// Задание 6
fun printUserInfo(user: User) {
    with(user) {
        println("""
            ИНФОРМАЦИЯ О ПОЛЬЗОВАТЕЛЕ
            Имя: $firstName $lastName
            Возраст: $age лет
            Email: ${email.orNotSpecified()}
            Телефон: ${phone.orNotSpecified()}
        """)
    }
}

// Задание 8
fun createStudents(): List<Student> {
    return listOf(
        Student("Алексей", "alex@edu.ru", "ИВТ-21", "+79111111111"),
        Student("Мария", null, "ПИ-22", "+79222222222"),
        Student("Денис", "denis@edu.ru", null, null),
        Student("Екатерина", null, null, "+79333333333"),
        Student("Павел", "pavel@edu.ru", "ИС-23", "+79444444444"),
        Student("Анастасия", null, "БИ-21", null)
    )
}

fun printStudents(students: List<Student>) {
    println("\nСПИСОК СТУДЕНТОВ")
    students.forEach { student ->
        println("""
            Студент: ${student.name}
              Email: ${student.email.orNotSpecified()}
              Группа: ${student.group.orNotSpecified()}
              Телефон: ${student.phone.orNotSpecified()}
        """.trimIndent())
    }
}

// Задание 9
fun createProducts(): List<Product> {
    return listOf(
        Product("Ноутбук", 15000.0, "Электроника"),
        Product("Смартфон", 12000.0, "Электроника"),
        Product("Наушники", 2500.0, "Аксессуары"),
        Product("Клавиатура", 1800.0, "Периферия"),
        Product("Монитор", 25000.0, "Электроника"),
        Product("Мышь", 800.0, "Периферия"),
        Product("Видеокарта", 80000.0, "Комплектующие"),
        Product("Процессор", 50000.0, "Комплектующие"),
        Product("Коврик для мыши", null, "Аксессуары"),
        Product("Веб-камера", 3500.0, "Периферия")
    )
}

// Задание 10
fun showProductCatalog(products: List<Product>) {
    println("\nКАТАЛОГ ТОВАРОВ")
    products.forEach { product ->
        println(product.toCardText())
    }
    
    println("\nДОРОГИЕ ТОВАРЫ (>10000 руб.)")
    products.expensiveProducts().forEach { product ->
        println("${product.name} - ${product.price.toPriceText()}")
    }
    
    println("\nТОВАРЫ БЕЗ ЦЕНЫ")
    products.filter { it.price == null }.forEach { product ->
        println(product.name)
    }
    
    println("\nСТАТИСТИКА ПО ТОВАРАМ")
    println("Средняя цена: ${products.averagePrice().toPriceText()}")
}

fun main() {
    // Задание 1
    println("ЗАДАНИЕ 1")
    val users = createUsers()
    
    println("Полная информация о пользователях:")
    users.forEach { user ->
        println("${user.fullName()}, ${user.age} лет, Email: ${user.email.orNotSpecified()}, Тел: ${user.phone.orNotSpecified()}")
    }
    
    println("\nСовершеннолетние пользователи:")
    users.filter { it.age.isAdult() }.forEach { user ->
        println(user.getShortInfo())
    }
    
    println("\nПользователи без email:")
    users.filter { it.email == null }.forEach { user ->
        println(user.fullName())
    }
    
    println("\nПользователи без телефона:")
    users.filter { it.phone == null }.forEach { user ->
        println(user.fullName())
    }
    
    // Задание 2
    println("\nЗАДАНИЕ 2")
    val movies = createMovies()
    
    println("Лучшие фильмы (рейтинг > 8):")
    movies.topRated().forEach { movie ->
        println("${movie.title} - ${movie.rating}")
    }
    
    println("\nКороткие фильмы (<100 минут):")
    movies.shortMovies().forEach { movie ->
        println("${movie.title} (${movie.duration} мин.)")
    }
    
    println("\nКарточки фильмов:")
    movies.forEach { movie ->
        println(movie.toCardText())
    }
    
    // Задание 3
    println("\nЗАДАНИЕ 3")
    val usersFromApply = createUsersWithApply()
    
    // Задание 4
    println("\nЗАДАНИЕ 4")
    findUserByEmail(users, "ivan@mail.ru")
    findUserByEmail(users, "nonexistent@mail.ru")
    
    // Задание 5
    println("\nЗАДАНИЕ 5")
    generateMoviesReport(movies)
    
    // Задание 6
    println("\nЗАДАНИЕ 6")
    printUserInfo(users[0])
    
    // Задание 7
    println("\nЗАДАНИЕ 7")
    println("Демонстрация мини-библиотеки расширений:")
    println("Сокращение строки: 'Очень длинное название для фильма'.shorten() -> ${"Очень длинное название для фильма".shorten()}")
    println("Проверка email: 'test@mail.ru'.isEmailValid() -> ${"test@mail.ru".isEmailValid()}")
    println("Формат цены: 1234.56.toPriceText() -> ${1234.56.toPriceText()}")
    println("Среднее значение списка: listOf(1,2,3,4,5).averageValue() -> ${listOf(1,2,3,4,5).averageValue()}")
    println("Квадрат числа: 7.square() -> ${7.square()}")
    
    // Задание 8
    println("\nЗАДАНИЕ 8")
    val students = createStudents()
    printStudents(students)
    
    // Задание 9
    println("\nЗАДАНИЕ 9")
    val products = createProducts()
    showProductCatalog(products)
    
    // Задание 10
    println("\nЗАДАНИЕ 10")
    println("Комплексная система с использованием всех scope-функций:")
    
    val demoUser = User("", "", 0, null, null).apply {
        firstName = "Тестовый"
        lastName = "Пользователь"
        age = 25
        email = "test@test.ru"
        phone = "+79999999999"
    }.also { 
        println("also: создан пользователь ${it.fullName()}")
    }.let { 
        println("let: проверка возраста - ${if (it.age.isAdult()) "совершеннолетний" else "несовершеннолетний"}")
        it
    }
    
    with(demoUser) {
        println("with: ${fullName()} - ${email.orNotSpecified()}")
    }
    
    val report = listOf(1, 2, 3, 4, 5).run {
        "run: сумма = ${sum()}, среднее = ${averageValue()}"
    }
    println(report)

    println("\nРАСШИРЕНИЯ ПОВЫШЕННОЙ СЛОЖНОСТИ")
    println("Средняя цена товаров: ${products.averagePrice().toPriceText()}")
    println("Средний рейтинг фильмов: ${movies.averageRating()}")
    println("Средний возраст пользователей: ${users.averageAge()}")
}
