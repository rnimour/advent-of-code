fun main() {

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val expectedValue = 1
    val expectedValue2 = 2
    val day = "07"
    val testInput = readInput("Day${day}_test")
    val test1 = part1(testInput)
    test1.println()
    check(test1 == expectedValue)
    // val test2 = part2(testInput)
    // test2.println()
    // check(test2 == expectedValue2)

    val input = readInput("Day$day")
    time("part 1") { part1(input) }.println()
    // time("part 2") { part2(input) }.println()
}
