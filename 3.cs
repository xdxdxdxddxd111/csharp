data class Game(
    val name: String,
    val genre: String,
    val price: Double?,
    val rating: Double,
    val description: String?
)

fun main() {
    // 2. Создаём список из 6+ игр разных жанров
    val games = listOf(
        Game("The Witcher 3", "RPG", 1999.0, 9.5, "Эпическая ролевая игра в открытом мире"),
        Game("Cyberpunk 2077", "RPG", null, 8.2, null),
        Game("Call of Duty: Modern Warfare", "Action", 2499.0, 8.8, "Шутер от первого лица"),
        Game("StarCraft II", "Strategy", 1499.0, 9.0, "Классическая стратегия в реальном времени"),
        Game("Resident Evil Village", "Horror", 2299.0, 8.5, "Хоррор с элементами выживания"),
        Game("Forza Horizon 5", "Racing", 2999.0, 9.2, "Гоночная игра с открытым миром"),
        Game("Hades", "Roguelike", 1299.0, 9.4, "Динамичная игра с процедурной генерацией уровней")
    )

    // 3. Выводим список игр с обработкой null
    println("=== СПИСОК ИГР ===")
    games.forEach { game ->
        val priceDisplay = game.price?.toString() ?: "Цена неизвестна"
        val descriptionDisplay = game.description ?: "Описание отсутствует"
        println("Название: ${game.name}")
        println("Жанр: ${game.genre}")
        println("Цена: $priceDisplay")
        println("Рейтинг: ${game.rating}")
        println("Описание: $descriptionDisplay")
        println("---")
    }

    // 4. Фильтрация
    println("\n=== ИГРЫ ЖАНРА RPG ===")
    val rpgGames = games.filter { it.genre == "RPG" }
    rpgGames.forEach { println(it.name) }

    println("\n=== ИГРЫ С РЕЙТИНГОМ ВЫШЕ 8 ===")
    val highRatedGames = games.filter { it.rating > 8.0 }
    highRatedGames.forEach { println("${it.name} (рейтинг: ${it.rating})") }

    // 5. Поиск игры по названию
    println("\n=== ПОИСК ИГРЫ ===")
    fun findGame(name: String) {
        val foundGame = games.find { it.name.equals(name, ignoreCase = true) }
        if (foundGame != null) {
            println("Найденная игра: ${foundGame.name}")
            println("Жанр: ${foundGame.genre}")
            println("Рейтинг: ${foundGame.rating}")
        } else {
            println("Игра не найдена")
        }
    }
    findGame("Cyberpunk 2077")
    findGame("Unknown Game")

    // 6. Использование when для жанров
    println("\n=== ТИПЫ ИГР ПО ЖАНРАМ ===")
    val uniqueGenres = games.map { it.genre }.toSet()
    uniqueGenres.forEach { genre ->
        val gameType = when (genre) {
            "RPG" -> "Ролевая игра"
            "Action" -> "Экшен"
            "Strategy" -> "Стратегическая игра"
            "Horror" -> "Хоррор"
            "Racing" -> "Гоночная игра"
            else -> "Другой жанр"
        }
        println("$genre -> $gameType")
    }
}


data class Movie(
    val title: String,
    val year: Int?,
    val genre: String?,
    val duration: Int?,
    val rating: Double?,
    val description: String?
)

// Продолжение в функции main()
fun extendedMain() {
    // 1. Создаём список фильмов
    val movies = listOf(
        Movie("Интерстеллар", 2014, "Фантастика", 169, 8.6, "Космическая эпопея о путешествиях через червоточины"),
        Movie("Побег из Шоушенка", 1994, "Драма", 142, 9.3, "История дружбы в тюрьме"),
        Movie("Начало", 2010, "Фантастика", 148, 8.8, "Фильм о снах внутри снов"),
        Movie("Джокер", 2019, "Драма", 122, 8.4, "История становления Джокера"),
        Movie("Дюна", 2021, "Фантастика", 155, 8.0, null),
        Movie("Топ Ган: Мэверик", 2022, "Боевик", 130, 8.3, "Продолжение культового фильма 80-х"),
        Movie("Оппенгеймер", 2023, "Биография", 180, 8.7, "История создателя атомной бомбы")
    )

    // 2. Выводим фильмы с шаблонами строк и Elvis оператором
    println("\n=== СПИСОК ФИЛЬМОВ ===")
    movies.forEach { movie ->
        println("""
            Название: ${movie.title}
            Год: ${movie.year ?: "Не указан"}
            Жанр: ${movie.genre ?: "Не указан"}
            Длительность: ${movie.duration ?: "Не указана"} мин.
            Рейтинг: ${movie.rating ?: "Нет рейтинга"}
            Описание: ${movie.description ?: "Описание отсутствует"}
        "".trimIndent())
    }

    // 3. Находим самый длинный фильм и фильм с максимальным рейтингом
    val longestMovie = movies.maxByOrNull { it.duration ?: 0 }
    val highestRatedMovie = movies.maxByOrNull { it.rating ?: 0.0 }

    println("\n=== АНАЛИЗ ФИЛЬМОВ ===")
    println("Самый длинный фильм: ${longestMovie?.title ?: "Не найден"} (${longestMovie?.duration} мин.)")
    println("Фильм с максимальным рейтингом: ${highestRatedMovie?.title ?: "Не найден"} (рейтинг: ${highestRatedMovie?.rating})")

    // 4. Средний рейтинг и количество фильмов по жанрам
    val averageRating = movies
        .map { it.rating }
        .filterNotNull()
        .average()
        .takeIf { !it.isNaN() } ?: 0.0

    val genreCounts = movies
        .groupBy { it.genre }
        .mapValues { it.value.size }

    println("Средний рейтинг: ${String.format("%.2f", averageRating)}")
    println("Количество фильмов по жанрам: $genreCounts")

    // 5. Фильмы после 2020 года
    val recentMovies = movies.filter { (it.year ?: 0) > 2020 }
    println("\n=== ФИЛЬМЫ ПОСЛЕ 2020 ГОДА ===")
    recentMovies.forEach { println("${it.title} (${it.year})") }
}

data class WarehouseItem(
    val name: String,
    val quantity: Int,
    val price: Double?,
    val category: String,
    val supplier: String?,
    val comment: String?
)

// Продолжение в функции extendedMain()
fun warehouseMain() {
    // 1. Создаём список товаров
    val warehouseItems = mutableListOf(
        WarehouseItem("Ноутбук ASUS", 3, 79999.0, "Электроника", "TechCorp", "Новый, в упаковке"),
        WarehouseItem("Мышь беспроводная", 15, 2499.0, "Электроника", "PeripheralsInc", null),
        WarehouseItem("Клавиатура механическая", 8, 4999.0, "Электроника", null, "Требуется проверка"),
        WarehouseItem("Стол офисный", 2, 12999.0, "Мебель", "FurnitureCo", "Повреждена упаковка"),
        WarehouseItem("Стул офисный", 5, 5999.0, "Мебель", "FurnitureCo", null),
        WarehouseItem("Монитор 27\"", 1, 24999.0, "Электроника", "DisplayTech", "Демонстрационный образец"),
        WarehouseItem("Принтер лазерный", 4, 14999.0, "Электроника", "OfficeSolutions", "Требуется заправка
                              WarehouseItem("Принтер лазерный", 4, 14999.0, "Электроника", "OfficeSolutions", "Требуется заправка"),
        WarehouseItem("Бумага А4", 50, 899.0, "Канцелярия", "PaperInc", null)
    )

    // 2. Выводим товары с разными фильтрами
    println("\n=== ТОВАРЫ С КОЛИЧЕСТВОМ МЕНЬШЕ 5 ===")
    val lowQuantityItems = warehouseItems.filter { it.quantity < 5 }
    lowQuantityItems.forEach { println("${it.name} (количество: ${it.quantity})") }

    println("\n=== ТОВАРЫ БЕЗ ПОСТАВЩИКА ===")
    val noSupplierItems = warehouseItems.filter { it.supplier == null }
    noSupplierItems.forEach { println(it.name) }

    println("\n=== ТОВАРЫ КАТЕГОРИИ 'ЭЛЕКТРОНИКА' ===")
    val electronicsItems = warehouseItems.filter { it.category == "Электроника" }
    electronicsItems.forEach { println("${it.name} (цена: ${it.price})") }

    // 3. Изменение цены для категории «Электроника» (уменьшение на 15 %)
    println("\n=== ИЗМЕНЁННЫЕ ЦЕНЫ НА ЭЛЕКТРОНИКУ (-15 %) ===")
    val updatedElectronics = electronicsItems.map { item ->
        item.copy(price = item.price?.times(0.85))
    }
    updatedElectronics.forEach { println("${it.name}: ${it.price} руб.") }

    // Обновляем основной список
    warehouseItems.removeAll { it.category == "Электроника" }
    warehouseItems.addAll(updatedElectronics)

    // 4. Поиск товара по части названия
    println("\n=== ПОИСК ТОВАРА ПО ЧАСТИ НАЗВАНИЯ ===")
    fun searchItem(partialName: String) {
        val foundItems = warehouseItems.filter { it.name.contains(partialName, ignoreCase = true) }
        if (foundItems.isNotEmpty()) {
            foundItems.forEach { println(it.name) }
        } else {
            println("Товар не найден")
        }
    }
    searchItem("мышь")
    searchItem("стол")

    // 5. Общая стоимость склада
    println("\n=== ОБЩАЯ СТОИМОСТЬ СКЛАДА ===")
    val totalValue = warehouseItems
        .map { (it.price ?: 0.0) * it.quantity }
        .sum()
    println("Общая стоимость: ${totalValue} руб.")
}

data class User(
    val login: String,
    val email: String?,
    val age: Int,
    val status: String,
    val phone: String?,
    val profileDescription: String?
)

fun usersMain() {
    // 1. Создаём список пользователей
    val users = listOf(
        User("admin_user", "admin@example.com", 35, "admin", "+79991234567", "Администратор системы"),
        User("moderator_1", "mod@example.com", 28, "moderator", null, "Модератор контента"),
        User("john_doe", "john@example.com", 22, "user", "+79997654321", "Обычный пользователь"),
        User("alice_smith", "alice@example.com", 30, "user", null, null),
        User("bob_wilson", "bob@example.com", 45, "user", "+79995555555", "Опытный пользователь"),
        User("guest_user", null, 18, "guest", null, "Гостевой аккаунт"),
        User("maria_ivanova", "maria@example.com", 27, "user", "+79998888888", "Активный пользователь"),
        User("peter_parker", "peter@example.com", 16, "user", "+79999999999", "Подросток"),
        User("anna_karenina", "anna@example.com", 50, "user", null, "Зрелый пользователь"),
        User("new_user", "new@example.com", 20, "user", "+79990000000", "Новый пользователь")
    )

    // 2. Реализуем фильтрацию и поиск
    println("\n=== ФИЛЬТРАЦИЯ ПО ВОЗРАСТУ (старше 30) ===")
    val olderUsers = users.filter { it.age > 30 }
    olderUsers.forEach { println("${it.login} (возраст: ${it.age})") }

    println("\n=== ПОИСК ПО ЛОГИНУ ===")
    fun findUserByLogin(login: String) {
        val user = users.find { it.login == login }
        if (user != null) {
            println("Найден пользователь: ${user.login}, возраст: ${user.age}")
        } else {
            println("Пользователь не найден")
        }
    }
    findUserByLogin("admin_user")
    findUserByLogin("unknown_user")

    println("\n=== ПОЛЬЗОВАТЕЛИ БЕЗ ТЕЛЕФОНА ===")
    val usersWithoutPhone = users.filter { it.phone == null }
    usersWithoutPhone.forEach { println(it.login) }

    // 3. Использование when для статусов
    println("\n=== ТИПЫ ПОЛЬЗОВАТЕЛЕЙ ПО СТАТУСАМ ===")
    users.forEach { user ->
        val statusDescription = when (user.status) {
            "admin" -> "Администратор — полный доступ"
            "moderator" -> "Модератор — ограниченный доступ"
            "user" -> "Обычный пользователь"
            "guest" -> "Гость — ограниченный функционал"
            else -> "Неизвестный статус"
        }
        println("${user.login}: $statusDescription")
    }

    // 4. Выводим статистику
    println("\n=== СТАТИСТИКА ПОЛЬЗОВАТЕЛЕЙ ===")
    println("Количество пользователей: ${users.size}")
    val averageAge = users.map { it.age }.average()
    println("Средний возраст: ${String.format("%.1f", averageAge)} лет")
    val oldestUser = users.maxByOrNull { it.age }
    println("Самый старший пользователь: ${oldestUser?.login} (${oldestUser?.age} лет)")
}
