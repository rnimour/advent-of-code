fun main() {
    fun part1(input: List<String>): Long {
        val ranges = makePairs(input)
        // var countOfNumberWeWouldHaveToGoThroughIfWeBruteForce = 0L
        // 2581194
        var sillyPatternsTotal = 0L
        for (range in ranges) {
            val start = range.first
            val end = range.second
            println("doing from $start to $end now")
            for (l in LongRange(start, end)) {
                if (l.isSillyPattern()) {
                    println("$l is silly!")
                    sillyPatternsTotal += l
                }
            }
            // countOfNumberWeWouldHaveToGoThroughIfWeBruteForce += end - start + 1
        }
        return sillyPatternsTotal
    }

    fun part2(input: List<String>): Long {
        val ranges = makePairs(input)
        var sillyPatternsTotal = 0L
        for (range in ranges) {
            val start = range.first
            val end = range.second
            println("doing from $start to $end now")
            for (l in LongRange(start, end)) {
                if (l.isReallySillyPattern()) {
                    println("$l is really silly!")
                    sillyPatternsTotal += l
                }
            }
        }
        return sillyPatternsTotal
    }

    // Small test from description, read test input from the `src/Day01_test.txt` file:
    val expectedValue = 1227775554L
    val testInput = readInput("Day02_test")
    // check(part1(testInput) == expectedValue)

    val expectedValue2 = 4174379265L
    check(part2(testInput) == expectedValue2)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}


fun makePairs(input: List<String>): List<Pair<Long, Long>> {
    return input[0].split(",")
        .map {
            val beginAndEnd = it.split("-")
            beginAndEnd[0].toLong() to beginAndEnd[1].toLong()
        }
}

// silly: if number is two numbers repeated twice
fun Long.isSillyPattern(): Boolean {
    val numberAsString = this.toString()
    val length = numberAsString.length
    if (length % 2 != 0) {
        return false // cannot be silly
    }
    return numberAsString.take(length / 2) == numberAsString.drop(length / 2)
}

// really silly: if number is any number repeated more than once
fun Long.isReallySillyPattern(): Boolean {
    val numberAsString = this.toString()
    val length = numberAsString.length
    for (amountOfRepetitions in 1..length / 2) {
        if (length % amountOfRepetitions != 0) {
            continue // cannot repeat this exact number of times
        }
        val parts = numberAsString.toParts(amountOfRepetitions)
        if (parts.areAllEqual()) {
            return true
        }
    }
    return false
}

private fun String.toParts(amount: Int): List<String> {
    val list = mutableListOf<String>()
    // println("string: $this. size: $length")
    // println("looping: ${(length / amount) downTo 2}")

    for (i in 0..<(length / amount)) {
        // println("\tlooping step i = $i:")
        // println("\t\t(i)   * amount = ${i * amount}")
        // println("\t\t(i+1) * amount = ${(i+1) * amount}")
        list.add(this.substring((i) * amount, (i+1) * amount))
    }
    return list.toList()
}

private fun List<String>.areAllEqual(): Boolean {
    if (this.size == 1) return false
    val first = this.firstOrNull()
    for (item in this) {
        if (item != first) {
            return false
        }
    }
    return true
}
