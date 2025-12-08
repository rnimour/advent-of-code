fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val maxJoltage = maxJoltage(it)
            // println("joltage: $maxJoltage")
            maxJoltage
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf {
            val maxJoltageTwelve = maxJoltageTwelve(it)
            // println("joltage: $maxJoltage")
            maxJoltageTwelve
        }
    }

    val expectedValue = 357
    val expectedValue2 = 3121910778619L
    val day = "03"
    val testInput = readInput("Day${day}_test")
    val test1 = part1(testInput)
    test1.println()
    check(test1 == expectedValue)
    val test2 = part2(testInput)
    test2.println()
    check(test2 == expectedValue2)

    val input = readInput("Day$day")
    time("part 1") { part1(input) }.println()
    time("part 2") { part2(input) }.println()
}

fun maxJoltage(bank: String): Int {
    for (i in 9 downTo 0) {
        val indexOfFirstHighNumber = bank.indexOfFirst { it.digitToInt() == i }
        if (indexOfFirstHighNumber == -1) continue // this number doesn't exist in the bank
        if (indexOfFirstHighNumber == bank.length - 1) continue // the highest number is the last number, can't make a high number
        val highestNumberThatIsNotTheLastNumber = bank[indexOfFirstHighNumber].digitToInt()
        val restOfBank = bank.drop(indexOfFirstHighNumber + 1)
        for (i in 9 downTo 0) {
            val indexOfSecondHighNumber = restOfBank.indexOfFirst { it.digitToInt() == i }
            if (indexOfSecondHighNumber == -1) continue // this number doesn't exist in the rest of the bank
            val highestNumberAfterFirstNumber = restOfBank[indexOfSecondHighNumber].digitToInt()
            return 10 * highestNumberThatIsNotTheLastNumber + highestNumberAfterFirstNumber
        }
    }
    throw IllegalStateException("not possible")
}

fun maxJoltageTwelve(bank: String): Long {
    val allIndices = bank.allIndices()
    var joltage = 0L
    var startIndex = -1
    for (battery in 1..12) {
        var powerOfTen = 1L
        repeat(12 - battery) { powerOfTen *= 10 }
        val indexOfFirstHighestNumberPossible =
            getIndexOfFirstHighestNumberPossible(
                allIndices,
                startIndex,
                battery,
                bank.length
            )
        startIndex = indexOfFirstHighestNumberPossible
        val number = bank[indexOfFirstHighestNumberPossible].digitToInt()

        joltage += powerOfTen * number
        // println(joltage)
    }
    return joltage
}

private fun getIndexOfFirstHighestNumberPossible(
    allIndices: List<List<Int>>,
    startIndex: Int,
    battery: Int,
    bankSize: Int,
): Int = allIndices.last {
    // it > startIndex: start searching after previous battery
    // it < bankSize - (12 - battery): need enough remaining batteries
    it.any { startIndex < it && it < bankSize - (12 - battery) }
}.first {it > startIndex}

private fun String.allIndices(): List<List<Int>> {
    val indices = List(10) { mutableListOf<Int>() }
    this.forEachIndexed { index, value ->
        indices[value.digitToInt()].add(index)
    }
    return indices
}
