var count = 0
var flag = false

fun callMethod() {
    println("Метод вызван")
    countCalls()
}

fun countCalls() {
    count++
    println("Количество вызовов: $count")
}

fun logMethod() {
    println("Вызов метода: ${java.time.LocalDateTime.now()}")
}

fun setFlag() {
    flag = true
    println("Флаг установлен: $flag")
}

fun main() {
    callMethod()
    callMethod()
    callMethod()

    logMethod()

    setFlag()
}
