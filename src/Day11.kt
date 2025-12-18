const val START = "you"
const val OUT = "out"
const val DAC = "dac"
const val FFT = "fft"
const val SERVER = "svr"

fun main() {

    fun part1(input: List<String>): Long {
        val startPoints = nextServers(START, input)
        return startPoints.sumOf {
            it.howManyPathsTo(OUT, input)
        }
    }

    fun part2v2(input: List<String>): Long {
        // find every path from SERVER to OUT which passes through both DAC and FFT
        val nodeToNumberOfPathsMap = mutableMapOf<NodeKey, Long>()

        val startPoints = nextServers(SERVER, input)
        return startPoints.sumOf {
            it.howManyPathsToRememberingNodesWithConditionsAndSavePath(
                OUT,
                input,
                nodeToNumberOfPathsMap,
                false,
                false
            )

        }
    }

    val expectedValue = 5L
    val expectedValue2 = 2L
    val day = "11"
    // val testInput = readInput("Day${day}_test")
    // val test1 = part1(testInput)
    // test1.println()
    // check(test1 == expectedValue)
    val testInput2 = readInput("Day${day}_test2")
    val test2 = part2v2(testInput2)
    test2.println()
    check(test2 == expectedValue2)

    val input = readInput("Day$day")
    // time("part 1") { part1(input) }.println()
    // time("part 2") { part2(input) }.println()
    time("part 2") { part2v2(input) }.println()
}

fun nextServers(
    currentServer: String,
    input: List<String>,
): List<String> =
    input.first { it.startsWith(currentServer) }
        .split(": ")[1] // get the outputs
        .split(" ")
        .map { it.trim() }


// assuming no loops
private fun String.howManyPathsTo(exit: String, input: List<String>): Long {
    var paths = 0L
    for (server in nextServers(this, input)) {
        if (server == exit) {
            paths++
        } else {
            paths += server.howManyPathsTo(exit, input)
        }
    }
    return paths
}

// assuming no loops
private fun String.howManyPathsToRememberingNodesWithConditionsAndSavePath(
    exit: String,
    input: List<String>,
    nodeToNumberOfPathsMap: MutableMap<NodeKey, Long>,
    visitedDAC: Boolean,
    visitedFFT: Boolean,
): Long {
    val nodeKey = NodeKey(this, visitedDAC, visitedFFT)
    if (this == exit) {
        return if (visitedDAC && visitedFFT) 1L else 0L
    }
    if (nodeToNumberOfPathsMap.contains(nodeKey)) {
        // TODO: prevent loops
        return nodeToNumberOfPathsMap[nodeKey]!!
    }

    var paths = 0L
    for (server in nextServers(this, input)) {
        paths += server.howManyPathsToRememberingNodesWithConditionsAndSavePath(
            exit = exit,
            input = input,
            nodeToNumberOfPathsMap = nodeToNumberOfPathsMap,
            visitedDAC = visitedDAC || this == DAC,
            visitedFFT = visitedFFT || this == FFT,
        )
    }
    nodeToNumberOfPathsMap[nodeKey] = paths
    return paths
}

data class NodeKey(
    val server: String,
    val visitedDAC: Boolean,
    val visitedFFT: Boolean,
)
