fun main() {
    fun part1(input: List<String>): Int = input.associate { line ->
        val (gameId, cubesPerGame) = game(line)
        val all = cubesPerGame.flatMap { cubesPerColor ->
            cubesPerColor.map { (color, count) ->
                count <= color.amount
            }
        }
        val possible = all.all { it }
        gameId to possible
    }
            .mapNotNull { (gameId, possible) -> gameId.takeIf { possible } }
            .sum()

    fun part2(input: List<String>): Int = input.sumOf { line ->
        val (_, cubesPerGame) = game(line)
        val fewestCubeNeeded = mutableMapOf<CubeColor, Int>()
        cubesPerGame.map { cubesPerSet ->
            cubesPerSet.forEach { (color, count) ->
                if ((fewestCubeNeeded[color] ?: 0) < count) {
                    fewestCubeNeeded[color] = count
                }
            }
        }
        val power = fewestCubeNeeded.map { (_, count) -> count }.fold(1) { acc, elemet ->
            acc * elemet
        }
        power
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun game(line: String): Pair<Int, List<Map<CubeColor, Int>>> {
    val gameIdMatcher = "Game (\\d*): ".toRegex()
    val gameId = gameIdMatcher.find(line)!!.groupValues[1].toInt()
    val sets = line.split(": ")[1]
    val cubesPerColor: List<Map<CubeColor, Int>> = sets.split("; ")
            .map { set ->
                CubeColor.entries.associateWith { cubeColor ->
                    cubeColor.cubeFinder.find(set)?.groupValues?.get(1)?.toInt() ?: 0
                }
            }
    return gameId to cubesPerColor
}

enum class CubeColor(val value: String, val amount: Int) {
    Red("red", 12), Green("green", 13), Blue("blue", 14);

    val cubeFinder = "(\\d*) $value".toRegex()
}