enum class TicketStatus(val description: String) {
    NEW("Новая заявка"),
    IN_PROGRESS("В обработке"),
    WAITING_FOR_USER("Ожидает ответа пользователя"),
    RESOLVED("Решена"),
    CLOSED("Закрыта")
}

data class Ticket(
    val number: Int,
    val userName: String,
    val subject: String,
    val status: TicketStatus,
    val priority: Int
)

fun printAllTickets(tickets: List<Ticket>) {
    println("\nВсе заявки")
    tickets.forEach { ticket ->
        println("№${ticket.number}: ${ticket.userName} - ${ticket.subject} [${ticket.status.description}] Приоритет: ${ticket.priority}")
    }
}

fun countTicketsByStatus(tickets: List<Ticket>) {
    println("\nСтатистика по статусам")
    val statusCount = tickets.groupingBy { it.status }.eachCount()
    statusCount.forEach { (status, count) ->
        println("${status.description}: $count")
    }
}

fun printOpenTickets(tickets: List<Ticket>) {
    println("\nОткрытые заявки")
    val openTickets = tickets.filter { 
        it.status == TicketStatus.NEW || 
        it.status == TicketStatus.IN_PROGRESS || 
        it.status == TicketStatus.WAITING_FOR_USER 
    }
    openTickets.forEach { ticket ->
        println("№${ticket.number}: ${ticket.userName} - ${ticket.subject}")
    }
}

fun printTicketStatusInfo(ticket: Ticket) {
    when (ticket.status) {
        TicketStatus.NEW -> println("Заявка №${ticket.number}: Только что создана, ожидает назначения")
        TicketStatus.IN_PROGRESS -> println("Заявка №${ticket.number}: Активно обрабатывается специалистом")
        TicketStatus.WAITING_FOR_USER -> println("Заявка №${ticket.number}: Ожидает ответа пользователя")
        TicketStatus.RESOLVED -> println("Заявка №${ticket.number}: Решена, ожидает подтверждения")
        TicketStatus.CLOSED -> println("Заявка №${ticket.number}: Закрыта")
    }
}

enum class UserRole(val roleName: String, val canEdit: Boolean, val canDelete: Boolean) {
    ADMIN("Администратор", true, true),
    MODERATOR("Модератор", true, false),
    TEACHER("Преподаватель", true, false),
    STUDENT("Студент", false, false),
    GUEST("Гость", false, false)
}

data class User(
    val login: String,
    val email: String,
    val role: UserRole
)

fun printAdmins(users: List<User>) {
    println("\nАдминистраторы")
    users.filter { it.role == UserRole.ADMIN }.forEach { user ->
        println("${user.login} (${user.email})")
    }
}

fun printTeachers(users: List<User>) {
    println("\nПреподаватели")
    users.filter { it.role == UserRole.TEACHER }.forEach { user ->
        println("${user.login} (${user.email})")
    }
}

fun printUsersWithDeletePermission(users: List<User>) {
    println("\nПользователи с правом удаления")
    users.filter { it.role.canDelete }.forEach { user ->
        println("${user.login} - ${user.role.roleName}")
    }
}

fun countUsersByRole(users: List<User>) {
    println("\nКоличество пользователей по ролям")
    val roleCount = users.groupingBy { it.role }.eachCount()
    roleCount.forEach { (role, count) ->
        println("${role.roleName}: $count")
    }
}

fun canDelete(user: User): Boolean {
    return user.role.canDelete
}

sealed class LoginResult {
    object Success : LoginResult()
    object WrongPassword : LoginResult()
    object UserBlocked : LoginResult()
    object ServerError : LoginResult()
}

fun login(login: String, password: String): LoginResult {
    val validUsers = mapOf("admin" to "123", "user" to "456", "teacher" to "789")
    val blockedUsers = listOf("blocked_user")
    
    return when {
        blockedUsers.contains(login) -> LoginResult.UserBlocked
        !validUsers.containsKey(login) -> LoginResult.WrongPassword
        validUsers[login] != password -> LoginResult.WrongPassword
        else -> LoginResult.Success
    }
}

fun processLoginResult(result: LoginResult) {
    when (result) {
        is LoginResult.Success -> println("Вход выполнен")
        is LoginResult.WrongPassword -> println("Неверный пароль")
        is LoginResult.UserBlocked -> println("Пользователь заблокирован")
        is LoginResult.ServerError -> println("Ошибка сервера")
    }
}

data class Book(
    val title: String,
    val author: String,
    val year: Int,
    val genre: String
)

sealed class SearchResult {
    data class Found(val book: Book) : SearchResult()
    object NotFound : SearchResult()
    object Error : SearchResult()
}

val catalog = listOf(
    Book("Война и мир", "Лев Толстой", 1869, "Роман"),
    Book("Преступление и наказание", "Фёдор Достоевский", 1866, "Роман"),
    Book("Мастер и Маргарита", "Михаил Булгаков", 1967, "Роман"),
    Book("Евгений Онегин", "Александр Пушкин", 1833, "Поэма"),
    Book("Мёртвые души", "Николай Гоголь", 1842, "Поэма"),
    Book("Тихий Дон", "Михаил Шолохов", 1940, "Роман"),
    Book("Доктор Живаго", "Борис Пастернак", 1957, "Роман"),
    Book("Архипелаг ГУЛАГ", "Александр Солженицын", 1973, "Документальная"),
    Book("Горе от ума", "Александр Грибоедов", 1825, "Комедия"),
    Book("Обломов", "Иван Гончаров", 1859, "Роман")
)

fun searchBook(query: String): SearchResult {
    if (query.isBlank()) return SearchResult.Error
    
    val foundBook = catalog.find { 
        it.title.contains(query, ignoreCase = true) || 
        it.author.contains(query, ignoreCase = true) 
    }
    
    return if (foundBook != null) SearchResult.Found(foundBook) else SearchResult.NotFound
}

fun processSearchResult(result: SearchResult) {
    when (result) {
        is SearchResult.Found -> println("Книга найдена: ${result.book.title} - ${result.book.author} (${result.book.year})")
        is SearchResult.NotFound -> println("Книга не найдена")
        is SearchResult.Error -> println("Ошибка поиска: пустой запрос")
    }
}

enum class CourseStatus {
    PLANNED,
    STARTED,
    IN_PROGRESS,
    FINISHED,
    ARCHIVED
}

data class Course(
    val name: String,
    val teacher: String,
    val status: CourseStatus
)

fun countCoursesByStatus(courses: List<Course>) {
    println("\nКоличество курсов по статусам")
    val statusCount = courses.groupingBy { it.status }.eachCount()
    statusCount.forEach { (status, count) ->
        println("$status: $count")
    }
}

fun printActiveCourses(courses: List<Course>) {
    println("\nАктивные курсы")
    val activeCourses = courses.filter { 
        it.status == CourseStatus.STARTED || it.status == CourseStatus.IN_PROGRESS 
    }
    activeCourses.forEach { course ->
        println("${course.name} - ${course.teacher}")
    }
}

fun printCourseStatusInfo(course: Course) {
    when (course.status) {
        CourseStatus.PLANNED -> println("Курс '${course.name}': Запланирован, скоро начнётся")
        CourseStatus.STARTED -> println("Курс '${course.name}': Только начался, присоединяйтесь!")
        CourseStatus.IN_PROGRESS -> println("Курс '${course.name}': Активно проводится")
        CourseStatus.FINISHED -> println("Курс '${course.name}': Завершён")
        CourseStatus.ARCHIVED -> println("Курс '${course.name}': В архиве")
    }
}

enum class PaymentMethod {
    CARD,
    CASH,
    SBP,
    CRYPTO
}

sealed class PaymentResult {
    object Success : PaymentResult()
    object Declined : PaymentResult()
    object Error : PaymentResult()
}

fun makePayment(amount: Double, method: PaymentMethod): PaymentResult {
    return when {
        amount <= 0 -> PaymentResult.Error
        amount > 100000 -> PaymentResult.Declined
        method == PaymentMethod.CRYPTO && amount < 100 -> PaymentResult.Declined
        else -> PaymentResult.Success
    }
}

fun processPaymentResult(result: PaymentResult, amount: Double, method: PaymentMethod) {
    when (result) {
        is PaymentResult.Success -> println("Платёж на сумму $amount руб. через ${method.name} успешно выполнен")
        is PaymentResult.Declined -> println("Платёж на сумму $amount руб. через ${method.name} отклонён")
        is PaymentResult.Error -> println("Ошибка при обработке платежа на сумму $amount руб.")
    }
}

enum class ServerStatus {
    ONLINE,
    OFFLINE,
    MAINTENANCE,
    OVERLOADED
}

data class Server(
    val name: String,
    val ipAddress: String,
    val status: ServerStatus
)

fun printWorkingServers(servers: List<Server>) {
    println("\nРабочие серверы")
    servers.filter { it.status == ServerStatus.ONLINE }.forEach { server ->
        println("${server.name} (${server.ipAddress})")
    }
}

fun printMaintenanceServers(servers: List<Server>) {
    println("\nСерверы на обслуживании")
    servers.filter { it.status == ServerStatus.MAINTENANCE }.forEach { server ->
        println("${server.name} (${server.ipAddress})")
    }
}

fun printOverloadedServers(servers: List<Server>) {
    println("\nПерегруженные серверы")
    servers.filter { it.status == ServerStatus.OVERLOADED }.forEach { server ->
        println("${server.name} (${server.ipAddress})")
    }
}

fun countServersByStatus(servers: List<Server>) {
    println("\nКоличество серверов по статусам")
    val statusCount = servers.groupingBy { it.status }.eachCount()
    statusCount.forEach { (status, count) ->
        println("$status: $count")
    }
}

fun printServerRecommendation(server: Server) {
    when (server.status) {
        ServerStatus.ONLINE -> println("${server.name}: Работа в штатном режиме")
        ServerStatus.OFFLINE -> println("${server.name}: Требуется проверка")
        ServerStatus.MAINTENANCE -> println("${server.name}: Плановое обслуживание")
        ServerStatus.OVERLOADED -> println("${server.name}: Необходимо перераспределить нагрузку")
    }
}

fun main() {
    // Задание 1
    val tickets = listOf(
        Ticket(1, "Иванов", "Не работает принтер", TicketStatus.NEW, 2),
        Ticket(2, "Петров", "Сброс пароля", TicketStatus.IN_PROGRESS, 1),
        Ticket(3, "Сидорова", "Вирус на компьютере", TicketStatus.WAITING_FOR_USER, 3),
        Ticket(4, "Кузнецов", "Не открывается сайт", TicketStatus.RESOLVED, 2),
        Ticket(5, "Васильева", "Проблема с почтой", TicketStatus.CLOSED, 1),
        Ticket(6, "Михайлов", "Не грузит Windows", TicketStatus.NEW, 3),
        Ticket(7, "Новикова", "Нужно установить программу", TicketStatus.IN_PROGRESS, 2),
        Ticket(8, "Соколов", "Не работает интернет", TicketStatus.WAITING_FOR_USER, 3),
        Ticket(9, "Морозова", "Ошибка в 1С", TicketStatus.NEW, 1),
        Ticket(10, "Волков", "Зависает компьютер", TicketStatus.IN_PROGRESS, 2)
    )
    
    printAllTickets(tickets)
    countTicketsByStatus(tickets)
    printOpenTickets(tickets)
    println("\nИнформация о заявках")
    tickets.take(5).forEach { printTicketStatusInfo(it) }
    
    // Задание 2
    val users = listOf(
        User("admin1", "admin1@top.ru", UserRole.ADMIN),
        User("admin2", "admin2@top.ru", UserRole.ADMIN),
        User("moder1", "moder@top.ru", UserRole.MODERATOR),
        User("teacher1", "ivanov@top.ru", UserRole.TEACHER),
        User("teacher2", "petrova@top.ru", UserRole.TEACHER),
        User("student1", "sidorov@top.ru", UserRole.STUDENT),
        User("student2", "kozlov@top.ru", UserRole.STUDENT),
        User("guest1", "guest@top.ru", UserRole.GUEST)
    )
    
    printAdmins(users)
    printTeachers(users)
    printUsersWithDeletePermission(users)
    countUsersByRole(users)
    println("\nПроверка прав удаления")
    users.take(4).forEach { user ->
        println("${user.login}: может удалять? ${if (canDelete(user)) "Да" else "Нет"}")
    }
    
    // Задание 3
    println("\nРезультаты авторизации")
    processLoginResult(login("admin", "123"))
    processLoginResult(login("user", "wrong"))
    processLoginResult(login("blocked_user", "123"))
    processLoginResult(login("unknown", "123"))
    
    // Задание 4
    println("\nПоиск книг")
    processSearchResult(searchBook("Толстой"))
    processSearchResult(searchBook("Евгений Онегин"))
    processSearchResult(searchBook("Несуществующая книга"))
    processSearchResult(searchBook(""))
    
    // Задание 5
    val courses = listOf(
        Course("Kotlin для начинающих", "Иванов И.И.", CourseStatus.PLANNED),
        Course("Java Базовый", "Петров П.П.", CourseStatus.STARTED),
        Course("Python для анализа данных", "Сидорова А.А.", CourseStatus.IN_PROGRESS),
        Course("Web разработка", "Кузнецов К.К.", CourseStatus.FINISHED),
        Course("Базы данных", "Васильева В.В.", CourseStatus.ARCHIVED),
        Course("Алгоритмы и структуры данных", "Михайлов М.М.", CourseStatus.STARTED),
        Course("Машинное обучение", "Новикова Н.Н.", CourseStatus.PLANNED),
        Course("DevOps практики", "Соколов С.С.", CourseStatus.IN_PROGRESS),
        Course("Мобильная разработка", "Морозова М.М.", CourseStatus.FINISHED),
        Course("Тестирование ПО", "Волков В.В.", CourseStatus.STARTED)
    )
    
    countCoursesByStatus(courses)
    printActiveCourses(courses)
    println("\nИнформация о курсах")
    courses.take(3).forEach { printCourseStatusInfo(it) }
    
    // Задание 6
    println("\nРезультаты платежей")
    processPaymentResult(makePayment(1000.0, PaymentMethod.CARD), 1000.0, PaymentMethod.CARD)
    processPaymentResult(makePayment(200000.0, PaymentMethod.SBP), 200000.0, PaymentMethod.SBP)
    processPaymentResult(makePayment(-50.0, PaymentMethod.CASH), -50.0, PaymentMethod.CASH)
    processPaymentResult(makePayment(50.0, PaymentMethod.CRYPTO), 50.0, PaymentMethod.CRYPTO)
    
    // Задание 7
    val servers = listOf(
        Server("WebServer1", "192.168.1.10", ServerStatus.ONLINE),
        Server("WebServer2", "192.168.1.11", ServerStatus.ONLINE),
        Server("DBServer", "192.168.1.20", ServerStatus.OVERLOADED),
        Server("FileServer", "192.168.1.30", ServerStatus.MAINTENANCE),
        Server("MailServer", "192.168.1.40", ServerStatus.OFFLINE),
        Server("BackupServer", "192.168.1.50", ServerStatus.ONLINE),
        Server("CacheServer", "192.168.1.60", ServerStatus.OVERLOADED),
        Server("MonitorServer", "192.168.1.70", ServerStatus.MAINTENANCE)
    )
    
    printWorkingServers(servers)
    printMaintenanceServers(servers)
    printOverloadedServers(servers)
    countServersByStatus(servers)
    println("\n=== Рекомендации администратору ===")
    servers.forEach { printServerRecommendation(it) }
}
