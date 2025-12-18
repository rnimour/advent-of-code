const val START = "you"
const val OUT = "out"

fun main() {

    fun part1(input: List<String>): Int {
        val startPoints = nextServers(START, input)
        return startPoints.sumOf {
            it.howManyPathsTo(OUT, input)
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val expectedValue = 5
    val expectedValue2 = 2
    val day = "11"
    val testInput = readInput("Day${day}_test")
    val test1 = part1(testInput)
    test1.println()
    check(test1 == expectedValue) // val test2 = part2(testInput)
    // test2.println()
    // check(test2 == expectedValue2)

    val input = readInput("Day$day")
    time("part 1") { part1(input) }.println() // time("part 2") { part2(input) }.println()
}

fun nextServers(currentServer: String, input: List<String>): List<String> =
    input.first { it.startsWith(currentServer) }.split(": ")[1] // get the outputs
        .split(" ").map { it.trim() }

private fun String.howManyPathsTo(exit: String, input: List<String>): Int {
    var paths = 0
    for (server in nextServers(this, input)) {
        if (server == exit) {
            paths++
        } else {
            paths += server.howManyPathsTo(exit, input)
        }
    }
    return paths
}
