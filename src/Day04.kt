fun main() {
    fun part1(input: List<String>): Int {
        // assuming square
        val gridSize = input.size
        val grid = List(gridSize) { mutableListOf<Char>() }
        for ((yIndex, y) in input.withIndex()) {
            for (x in y) {
                grid[yIndex].add(x)
            }
        }
        // grid[y][x]
        // grid.print()

        return markRemovableSpotsAndReturnHowMany(input, grid)
    }

    fun part2(input: List<String>): Int {
        // assuming square
        val gridSize = input.size
        val grid = List(gridSize) { mutableListOf<Char>() }
        for ((yIndex, y) in input.withIndex()) {
            for (x in y) {
                grid[yIndex].add(x)
            }
        }
        // grid[y][x]
        // grid.print()

        var possibleSpots = 0
        do {
            removeRemovableSpots(grid)
            possibleSpots += markRemovableSpotsAndReturnHowMany(input, grid)
        } while (grid.any { it.any { it == 'x' } }) // while there are still spots able to remove

        // grid.print()

        return possibleSpots
    }

    val expectedValue = 13
    val expectedValue2 = 43
    val day = "04"
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

fun removeRemovableSpots(grid: List<MutableList<Char>>) {
    for (yIndex in grid.indices) {
        for (xIndex in grid.indices) {
            if (grid[yIndex][xIndex] == 'x') {
                grid[yIndex][xIndex] = '.'
            }
        }
    }
}

private fun markRemovableSpotsAndReturnHowMany(
    input: List<String>,
    grid: List<MutableList<Char>>
): Int {
    var possibleSpots = 0
    for (yIndex in input.indices) {
        for (xIndex in input[yIndex].indices) {
            if (grid[yIndex][xIndex] == '.') {
                continue
            }
            if (numOfNeighborsOf(xIndex, yIndex, grid) < 4) {
                possibleSpots++
                grid[yIndex][xIndex] = 'x'
            }
        }
    }
    return possibleSpots
}

fun numOfNeighborsOf(xIndex: Int, yIndex: Int, grid: List<MutableList<Char>>): Int {
    var neighbours = 0
    for (xNeighbour in rangePlusMinusOneClampToGrid(xIndex, grid)) {
        for (yNeighbour in rangePlusMinusOneClampToGrid(yIndex, grid)) {
            if (xNeighbour == xIndex && yNeighbour == yIndex) {
                continue // don't count yourself
            }
            val neighbour = grid[yNeighbour][xNeighbour]
            if (neighbour == '@' || neighbour == 'x') {
                neighbours++
            }
        }
    }
    return neighbours
}

private fun rangePlusMinusOneClampToGrid(xIndex: Int, grid: List<MutableList<Char>>): IntRange =
    maxOf(0, xIndex - 1)..minOf(grid.size - 1, xIndex + 1)

private fun List<MutableList<Char>>.print() {
    println("\n==================\n")
    for ((yIndex, y) in this.withIndex()) {
        for (x in this[yIndex]) {
            print(if (x == '.') "· " else "$x ")
        }
        println("")
    }
    println("\n==================\n")
}

