package cinema

import kotlin.system.exitProcess

fun main() {
    // write your code here
    val (row, column) = getSeatingInformation()
    val matrix = Array(row) { Array(column) { 'S' } }

    while (true) {
        println()
        println(
            "1. Show the seats\n" +
                    "2. Buy a ticket\n" +
                    "3. Statistics\n" +
                    "0. Exit"
        )
        val input = readln().toInt()
        when (input) {
            1 -> printSeatingArrangement(row, column, matrix)
            2 -> {
                val ticketPrice = getTicketInformation(row, column, matrix)
                println()
                println("Ticket price: $$ticketPrice")
            }

            3 -> statistics(row, column, matrix)
            0 -> return
        }
    }
}

fun statistics(row: Int, column: Int, matrix: Array<Array<Char>>) {
    var (income, purchasedTickets) = currentIncome(row, column, matrix)
    val percentage = "%.2f".format((purchasedTickets.toDouble() / (row * column)) * 100)
    println()
    println(
        "Number of purchased tickets: $purchasedTickets\n" +
                "Percentage: $percentage%\n" +
                "Current income: \$$income\n" +
                "Total income: \$${totalIncome(row, column)}"
    )
}

fun getSeatingInformation(): IntArray {
    println("Enter the number of rows:")
    val row = readln().toInt()
    println("Enter the number of seats in each row:")
    val column = readln().toInt()
    return intArrayOf(row, column)
}

fun getTicketInformation(row: Int, column: Int, matrix: Array<Array<Char>>): Int {
    println()
    var seatRow: Int
    var seatCol: Int
    while (true) {
        println()
        println("Enter a row number:")
        seatRow = readln().toInt()
        println("Enter a seat number in that row:")
        seatCol = readln().toInt()
        if (seatRow > row || seatCol > column) {
            println("\nWrong input!")
        } else if (matrix[seatRow - 1][seatCol - 1] != 'S') {
            println("That ticket has already been purchased!")
        } else {
            break
        }
    }

    return calculateTicketPrice(row, column, seatRow, seatCol, matrix)

}

fun printSeatingArrangement(row: Int, column: Int, matrix: Array<Array<Char>>) {
    println()
    println("Cinema:")
    print("  ")
    for (i in 1..column) {
        print("$i ")
    }
    println()
    for ((rowIndex, rowMatrix) in matrix.withIndex()) {
        print("${rowIndex + 1} ")
        for (seat in rowMatrix) {
            print("$seat ")
        }
        println()
    }
}

fun calculateTicketPrice(row: Int, column: Int, seatRow: Int, seatCol: Int, matrix: Array<Array<Char>>): Int {
    val seatCount = row * column
    val ticketPrice = when (seatCount) {
        in 0..60 -> 10
        else ->
            if (seatRow > (row / 2)) {
                8
            } else {
                10
            }
    }

    matrix[seatRow - 1][seatCol - 1] = 'B'
    return ticketPrice
}

fun totalIncome(row: Int, column: Int): Int {
    val seatCount = row * column
    val frontHalfSeat = (row / 2) * column
    val backHalfSeat = (row - (row / 2)) * column
    val totalIncome = when (seatCount) {
        in 0..60 -> 10 * seatCount
        else -> 8 * backHalfSeat + 10 * frontHalfSeat
    }
    return totalIncome
}

fun currentIncome(row: Int, column: Int, matrix: Array<Array<Char>>): IntArray {
    var income = 0
    var ticketCount = 0
    for ((rowIndex, rowMatrix) in matrix.withIndex()) {
        for ((colIndex, rowElement) in rowMatrix.withIndex()) {
            if (rowElement == 'B') {
                income += calculateTicketPrice(row, column, rowIndex + 1, colIndex + 1, matrix)
                ticketCount++
            }

        }
    }
    return intArrayOf(income, ticketCount)
}