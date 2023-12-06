fun main() {

    fun timesToWin(input: List<Pair<Long, Long>>): Long = input.map { (raceTime, distanceRecord) ->
        (0..raceTime).reduce { acc, l ->
            val distance = l * (raceTime - l)
            if (distance > distanceRecord) {
                acc + 1
            } else {
                acc
            }
        }
    }.fold(1) { acc, i -> acc * i }

    fun part1(input: List<String>): Int {
        fun toNumbers(input: String): List<Long> = input.substringAfter(':')
                .split(' ')
                .filter { it.isNotBlank() }
                .map { it.toLong() }

        val times = toNumbers(input[0])
        val records = toNumbers(input[1])
        val races = buildList {
            times.forEachIndexed { index, value ->
                add(value to records[index])
            }
        }
        val result = timesToWin(races)
        return result.toInt()
    }

    fun part2(input: List<String>): Int {
        fun toNumber(input: String): Long = input.substringAfter(':')
                .split(' ')
                .filter { it.isNotBlank() }
                .joinToString(separator = "")
                .toLong()

        val time = toNumber(input[0])
        val record = toNumber(input[1])

        val result = timesToWin(listOf(time to record))

        return result.toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}