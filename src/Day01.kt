fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        line.filter { it.isDigit() }
                .let {
                    buildString {
                        append(it.first())
                        append(it.last())
                    }
                }
                .toInt()
    }

    fun part2(input: List<String>): Int = input.sumOf { line ->

         val toValue: (Pair<Int, String>) -> Pair<Int, Int> = { (index, word) -> index to lookupTable.indexOf(word).plus(1) }

         val firstWord = line.findAnyOf(lookupTable)?.let(toValue) ?: (Int.MAX_VALUE to -1)
         val lastWord = line.findLastAnyOf(lookupTable)?.let(toValue) ?: (Int.MIN_VALUE to -1)

         val firstDigit = line.firstOrNull { it.isDigit() }?.let { line.indexOf(it) to it.digitToInt() }
                 ?: (Int.MAX_VALUE to -1)
         val lastDigit = line.lastOrNull { it.isDigit() }?.let { line.lastIndexOf(it) to it.digitToInt() }
                 ?: (Int.MIN_VALUE to -1)

         val firstValue = when {
             firstWord.first < firstDigit.first -> firstWord.second
             firstDigit.first < firstWord.first -> firstDigit.second
             else -> error("line $line doesn't contain digit or word")
         }

         val lastValue = when {
             lastWord.first > lastDigit.first -> lastWord.second
             lastDigit.first > lastWord.first -> lastDigit.second
             else -> error("line $line doesn't contain digit or word")
         }
         val calibrationValue = "$firstValue$lastValue".toInt()
         println("$line: $calibrationValue")
         calibrationValue
     }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

val lookupTable = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
)
