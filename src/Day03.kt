fun main() {
    fun partsPerLine(input: List<String>): Map<Int, List<Pair<Int, IntRange>>> {
        val numberFinder = "\\d+".toRegex()
        return input.mapIndexed { index, line ->
            val matches = numberFinder.findAll(line)
                .mapNotNull { matchResult ->
                    matchResult.groups.firstOrNull()?.let { it.value.toInt() to it.range }
                }
                .toList()
            index to matches
        }.toMap()
    }

    fun rangePreSuccessor(range: IntRange, line: String) = IntRange(
        range.first.minus(1).coerceAtLeast(0),
        range.last.plus(1).coerceAtMost(line.length - 1)
    )

    fun part1(input: List<String>): Int {
        val partsPerLine = partsPerLine(input)

        return partsPerLine.mapNotNull { (lineIndex, values) ->
            val lineBefore = (lineIndex - 1).takeIf { it >= 0 }?.let { input[it] }
            val lineAfter = (lineIndex + 1).takeIf { it < input.size - 1 }?.let { input[it] }
            val line = input[lineIndex]
            val lines = listOfNotNull(lineBefore, line, lineAfter)
            values.mapNotNull { (value, range) ->
                val rangePreSuccessor = rangePreSuccessor(range, line)

                val isComponent = lines.any { componentString ->
                    componentString.substring(rangePreSuccessor)
                        .any { !it.isDigit() && it != '.' }
                }

                value.takeIf { isComponent }
            }.takeIf { values.isNotEmpty() }
                .orEmpty()
                .sum()

        }.sum()
    }

    fun part2(input: List<String>): Int {
        val gearInputs = input.flatMapIndexed { lineIndex, line ->
            "\\*".toRegex().findAll(line)
                .map { matchResult -> matchResult.range.first }
                .map { index -> lineIndex to index }
        }
        val partsPerLine = partsPerLine(input)


        val map: MutableMap<Pair<Int, Int>, List<Int>> = mutableMapOf()

        partsPerLine.mapNotNull { (lineIndex, values) ->
            val lineIndexBefore = (lineIndex - 1).takeIf { it >= 0 }
            val lineIndexAfter = (lineIndex + 1).takeIf { it < input.size - 1 }

            val lineBefore = lineIndexBefore?.let { input[it] }
            val lineAfter = lineIndexAfter?.let { input[it] }
            val line = input[lineIndex]

            val lines = listOfNotNull(lineBefore, line, lineAfter)

            values.forEach { (value, range) ->
                val rangePreSuccessor = rangePreSuccessor(range, line)

                listOfNotNull(lineIndexBefore, lineIndex, lineIndexAfter).map { index ->
                    gearInputs.filter { it.first == index && rangePreSuccessor.contains(it.second) }
                        .forEach {
                            map[it] = map[it].orEmpty().toMutableList().plus(value)
                        }
                }
            }
        }

        map.forEach { (gear, list) ->
            if (list.size > 2) {
                error("$list on gear $gear")
            }
        }

        return map.filterValues { it.size == 2 }
            .values
            .sumOf { list -> list.reduce { acc, i -> acc * i } }


    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}