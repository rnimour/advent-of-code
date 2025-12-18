const val START = "you"
const val OUT = "out"
const val DAC = "dac"
const val FFT = "fft"
const val SERVER = "svr"

fun main() {

    fun part1(input: List<String>): Int {
        val startPoints = nextServers(START, input)
        return startPoints.sumOf {
            it.howManyPathsTo(OUT, input)
        }
    }

    fun part2(input: List<String>): Int {
        // find every path from SERVER to OUT which passes through both DAC and FFT
        val startPoints = nextServers(SERVER, input)
        return startPoints.sumOf {
            it.howManyPathsToWithConditions(
                OUT,
                input,
                emptySet(),
                setOf(DAC, FFT)
            ).first
        }
    }

    val expectedValue = 5
    val expectedValue2 = 2
    val day = "11"
    val testInput = readInput("Day${day}_test")
    val testInput2 = readInput("Day${day}_test2")
    // val test1 = part1(testInput)
    // test1.println()
    // check(test1 == expectedValue)
    val test2 = part2(testInput2)
    test2.println()
    check(test2 == expectedValue2)

    val input = readInput("Day$day")
    // time("part 1") { part1(input) }.println()
    time("part 2") { part2(input) }.println()
}

fun nextServers(currentServer: String, input: List<String>): List<String> =
    input.first { it.startsWith(currentServer) }.split(": ")[1] // get the outputs
        .split(" ").map { it.trim() }

// assuming no loops
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

// private fun String.howManyPathsToRememberNodes(
//     exit: String,
//     input: List<String>,
//     visitedNodes: Set<String>,
// ): Pair<Int, Set<String>> {
//     var paths = 0
//     val visitedNodes: Set<String> = visitedNodes + this
//     for (server in nextServers(this, input)) {
//         if (server == exit) {
//             paths++
//         } else {
//             paths += server.howManyPathsToRememberNodes(exit, input, HashSet(visitedNodes)).first
//         }
//     }
//     return Pair(paths, visitedNodes)
// }

private fun String.howManyPathsToWithConditions(
    exit: String,
    input: List<String>,
    visitedNodes: Set<String>,
    requiredNodes: Set<String>,
): Pair<Int, Set<String>> {
    var paths = 0
    val visitedNodes: Set<String> = visitedNodes + this
    // visitedNodes.println()
    for (server in nextServers(this, input)) {
        if (server == exit) {
            if (visitedNodes.containsAll(requiredNodes)) {
                paths++
                // print("!!!! ")
            }
            // (visitedNodes + exit).println()
        } else {
            paths += server.howManyPathsToWithConditions(exit, input, visitedNodes, requiredNodes).first
        }
    }

    return Pair(paths, visitedNodes)
}

