import com.sun.org.apache.xpath.internal.operations.Or
import jdk.internal.vm.vector.VectorSupport.test

fun main() {

    fun part1(input: List<String>): Int {
        var pos = 50
        var count = 0
        for (line in input) {
            print("instr: $line        pos = $pos -> ")
            val value = line.drop(1).toInt()
            pos = when (line[0]) {
                'L' -> (pos - value + 100) % 100
                'R' -> (pos + value) % 100
                else -> throw IllegalArgumentException("Invalid direction")
            }
            print("$pos")
            if (pos == 0) {
                count++
                print("  <-- COUNTED!")
            }
            println()
        }

        return count
    }

    // this is quite possibly the shittiest code I've ever written
    fun part2(input: List<String>): Int {
        var pos = 50
        var count = 0
        fun printlnCount() {
            println("                     ### ct = $count")
        }

        lines@ for (line in input) {
            println("instr: $line")
            print("    pos = $pos -> ")
            val value = line.drop(1).toInt()
            var oldPos = pos
            pos = when (line[0]) {
                'L' -> (pos - value)
                'R' -> (pos + value)
                else -> throw IllegalArgumentException("Invalid direction")
            }
            println("$pos")
            if (pos == 0) {
                count++
                println("            exact 0!")
                printlnCount()
                continue
            }
            if (pos >= 100) {
                while (pos >= 100) {
                    pos -= 100
                    count++
                    println("            \\-> $pos")
                    printlnCount()
                }
                continue@lines
            }
            while (pos < 0) {
                if (oldPos != 0) {
                    pos += 100
                    count++
                    println("            /-> $pos")
                    printlnCount()
                } else {
                    pos += 100
                    println("            /-> $pos         started @ zero! not counted")
                    println("                     === ct = $count")
                    oldPos = pos
                }
            }
            if (pos == 0) {
                count++
                println("            exact 0!")
                printlnCount()
            }
        }

        println("//////////////////")
        return count
    }

    //
    // the stupid way. at least it works
    fun part3(input: List<String>): Int {

        var pos = 50
        var count = 0
        for (line in input) {
            println("instr: $line\n    pos = $pos")
            val (direction, amount) = Pair(line[0], line.drop(1).toInt())
            repeat(amount) {
                pos = when (direction) {
                    'L' -> (pos - 1 + 100) % 100
                    'R' -> (pos + 1) % 100
                    else -> throw IllegalArgumentException("Invalid direction")
                }
                if (pos == 0) {
                    count++
                    println("    hit 0!")
                    println("        ct = $count")
                }
            }
        }

        return count

    }

    //

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    part2(testInput).println()
    check(part2(testInput) == 6)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
    part3(input).println()
}
