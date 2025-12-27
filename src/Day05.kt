import java.math.BigDecimal

fun main() {
    fun part1(input: List<String>): Int {
        val (freshIngredientRanges, ingredients) = readIngredientsAndRanges(input)

        return ingredients.count { ingredient ->
            freshIngredientRanges.any { ingredient in it }
        }
    }

    fun part2(input: List<String>): BigDecimal {
        val (freshIngredientRanges, ingredients) = readIngredientsAndRanges(input)
        // I misunderstood the assignment :sobbing_cat: I thought we needed to do below
        // val rangesWhichContainFreshIngredients =
        //     freshIngredientRanges.filter { range ->
        //         ingredients.any { ingredient ->
        //             ingredient in range
        //         }
        //     }
        // println("number of ranges which contain fresh ingredients: ${rangesWhichContainFreshIngredients.size}")
        // rangesWhichContainFreshIngredientsWithoutOverlap. Ordered.
        val rangesWithoutOverlap = mutableListOf<LongRange>()
        for (range in freshIngredientRanges.sortedBy { it.first }) {
            if (rangesWithoutOverlap.isEmpty()) {
                rangesWithoutOverlap.add(range)
                continue
            }
            val lastRange = rangesWithoutOverlap.last()
            if (range.first > lastRange.last) {
                // no overlap
                rangesWithoutOverlap.add(range)
                // println("no overlap between")
                // println("old:  $lastRange")
                // println("new:  $range")
                continue
            }
            // overlap
            if (range.last <= lastRange.last) {
                // fully contained
                // println("fully contained:")
                // println("old:  $lastRange")
                // println("new:  $range")
                continue
            }
            // partially overlapping
            // println("partially overlapping:")
            // println("old:  $lastRange")
            // println("new:  $range")
            val updatedRange = LongRange(
                lastRange.first,
                range.last
            )
            println("upd:  $updatedRange")
            rangesWithoutOverlap.removeLast()
            rangesWithoutOverlap.add(updatedRange)
        }
        //
        // println("number of ranges without overlap: ${rangesWithoutOverlap.size}")
        // println("ranges:")
        for (r in rangesWithoutOverlap) {
            println("  $r")
        }

        return rangesWithoutOverlap.sumOf { it.last.toBigDecimal() - it.first.toBigDecimal() + BigDecimal.ONE }
    }

    val expectedValue = 3
    val expectedValue2 = 14.toBigDecimal()
    val day = "05"
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

private fun readIngredientsAndRanges(input: List<String>): Pair<MutableList<LongRange>, MutableList<Long>> {
    val freshIngredientRanges = mutableListOf<LongRange>()
    val ingredients = mutableListOf<Long>()
    for (line in input) {
        if (line.contains("-")) {
            // range
            val split = line.split("-")
            freshIngredientRanges.add(
                LongRange(
                    split[0].toLong(),
                    split[1].toLong()
                )
            )
        } else if (line.isNotEmpty()) {
            // ingredient
            ingredients.add(line.toLong())
        }
    }
    return Pair(freshIngredientRanges, ingredients)
}
