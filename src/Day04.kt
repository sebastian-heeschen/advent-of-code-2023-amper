fun main() {

    fun matchingNumbers(line: String): Int {
        val (winningNumbers, ownedNumbers) = line.split(':')[1]
                .split('|')
                .map { numbers ->
                    numbers.split(" ")
                            .filter { it.isNotBlank() }
                            .map { it.trim().toInt() }
                }
        return winningNumbers.intersect(ownedNumbers.toSet()).size
    }

    fun part1(input: List<String>): Int = input.sumOf { line ->
        val matchingNumbers = matchingNumbers(line)

        val pointsPerCard = if (matchingNumbers > 0) (1..matchingNumbers).reduce { acc, _ -> acc * 2 } else 0
        pointsPerCard
    }


    fun part2(input: List<String>): Int {

        val cards = IntArray(input.size)
        cards.fill(1)

        return input.withIndex().sumOf { (i, line) ->
            val matchingNumbers = matchingNumbers(line)
            for (j in i + 1..i + matchingNumbers) {
                cards[j] += cards[i]
            }
            cards[i]
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}