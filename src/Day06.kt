import jdk.internal.net.http.common.Pair.pair
import java.math.BigInteger

fun main() {
    fun part1(input: List<String>): Long {
        val calculations = parseInput(input)
        var result = 0L
        for ((nums, op) in calculations) {
            result += nums.reduce { acc, i -> if (op == Op.ADD) acc + i else acc * i }
        }
        return result
    }

    fun part2(input: List<String>): BigInteger {
        // well shit.
        var result = BigInteger.ZERO
        for ((listOfListOfnums, op) in realParseInput(input)) {
            val sub = listOfListOfnums
                .map { getValueFromColumn(it).toLong() }
                .filter { !(op == Op.MULT && it == 0L) }
                .reduce { acc, i ->
                    when (op) {
                        Op.ADD -> acc + i
                        Op.MULT -> acc * i
                    }
                }.toBigInteger()
            result += sub
            if (result < 0.toBigInteger()) {
                throw RuntimeException("what")
            }
        }
        return result
    }

    // val test = listOf(listOf(1,2,3),listOf(4,5,6))
    // test.println()
    // test.transpose().println()
    // return

    val expectedValue = 4277556L
    val expectedValue2 = 3263827.toBigInteger()
    val day = "06"
    val testInput = readInput("Day${day}_test")
    val test1 = part1(testInput)
    test1.println()
    check(test1 == expectedValue)
    val test2 = part2(testInput)
    test2.println()
    check(test2 == expectedValue2)

    val input = readInput("Day$day")
    time("part 1") { part1(input) }.println()
    // 4693159084994 is too low for part 2:
    // 30144547951 is too low for part 2:
    time("part 2") { part2(input) }.println()
}

fun getValueFromColumn(column: List<Int>): Int {
    if (column.isEmpty()) return 0
    var result = 0
    for ((index, digit) in column.withIndex()) {
        var digitWithCorrectPowerOfTen = digit
        repeat(column.size - index - 1) { digitWithCorrectPowerOfTen *= 10 }
        result += digitWithCorrectPowerOfTen
    }
    return result
}

fun realParseInput(input: List<String>): List<Pair<List<List<Int>>, Op>> {
    val opIndex = input.indexOfFirst { isOp(it) }
    var startIndex = 0
    var endIndexThisOp: Int

    val allOps = mutableListOf<Op>()
    val allNums = mutableListOf<List<List<Int>>>()
    while (startIndex < input[0].length) {
        allOps.add(Op.fromChar(input[opIndex][startIndex]))
        val numsThisOp = MutableList(5) { mutableListOf<Int>() }

        val nextOpIndex = input[opIndex].drop(startIndex + 1).indexOfFirst { it.isOp() }
        endIndexThisOp = if (nextOpIndex == -1) input[0].length else nextOpIndex + startIndex + 1
        while (startIndex < endIndexThisOp) {
            val numsThisColumn = mutableListOf<Int>()
            for (verticalIndex in 0..<opIndex) {
                val ch = input[verticalIndex][startIndex]
                if (ch.isDigit()) {
                    numsThisColumn.add(ch.digitToInt())
                }
            }
            if (numsThisColumn.isNotEmpty()) {
                numsThisOp.add(numsThisColumn)
            }

            startIndex++
        }
        allNums.add(numsThisOp)
    }
    return allOps.withIndex().map { (index, op) ->
        Pair(allNums[index], op)
    }
}

private fun Char.isOp(): Boolean =
    Op.entries.any { it.ch == this }

private fun isOp(string: String): Boolean = string.startsWith("+") || string.startsWith("*")


fun parseInput(input: List<String>): MutableList<Pair<List<Long>, Op>> {
    val numOfNums = input[0].split(" ").filter { it.isNotBlank() }.size

    val rowsOfNums = List(numOfNums) { mutableListOf<Long>() }
    val ops = mutableListOf<Op>()
    for ((index, line) in input.dropLast(1).withIndex()) {
        rowsOfNums[index].addAll(
            line.split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toLong() }
        )
    }
    input.last().split(" ").filter { it.isNotBlank() }.map {
        ops.add(
            when (it) {
                "+" -> Op.ADD
                "*" -> Op.MULT
                else -> throw IllegalStateException("unknown operation: $it")
            }
        )
    }

    val columnsOfNums = rowsOfNums.transpose()
    val numsAndOps = mutableListOf<Pair<List<Long>, Op>>()
    for (i in columnsOfNums.indices) {
        numsAndOps.add(Pair(columnsOfNums[i], ops[i]))
    }
    return numsAndOps
}

private fun List<List<Long>>.transpose(): List<List<Long>> {
    val transposed = List(this[0].size) { mutableListOf<Long>() }
    for ((rowIndex, row) in this.withIndex()) {
        for ((lineIndex, line) in row.withIndex()) {
            transposed[lineIndex].add(this[rowIndex][lineIndex])
        }
    }
    return transposed
}

enum class Op(val ch: Char) {
    ADD('+'),
    MULT('*');

    companion object {
        fun fromChar(ch: Char): Op = entries.first { it.ch == ch }
    }
}

