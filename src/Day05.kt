fun main() {
    fun parseLineToSeeds(line: String) = line
            .split(' ')
            .map { it.trim().toLong() }

    fun part1(input: List<String>): Int {

        val seeds = input[0].substringAfter(":").trim().split(" ").map { it.toLong() }

        data class Range(val dst: Long, val src: Long, val len: Long)
        data class Dst(val name: String, val ranges: List<Range>)

        val map = HashMap<String, Dst>()
        input.drop(2).parts { p ->
            val (sn, dn) = Regex("(\\w+)-to-(\\w+) map:").matchEntire(p[0])!!.destructured
            val ranges = p.drop(1).map { s ->
                val (dst, src, len) = s.split(" ").map { it.toLong() }
                Range(dst, src, len)
            }
            map[sn] = Dst(dn, ranges)
        }
        fun convert(cn: String, x: Long): Pair<String, Long>? {
            val (dn, ranges) = map[cn] ?: return null
            for (r in ranges) {
                if (x in r.src..<r.src + r.len)
                    return dn to x - r.src + r.dst
            }
            return dn to x
        }

        val result = seeds.minOf { seed ->
            var cn = "seed"
            var x = seed
            while (true) {
                val r = convert(cn, x) ?: break
                cn = r.first
                x = r.second
            }
            check(cn == "location")
            x
        }
        return result.toInt()
    }

    fun part2(input: List<String>): Int {
        val seeds = input[0].substringAfter(":").trim().split(" ").map { it.toLong() }

        data class Range(val dst: Long, val src: Long, val len: Long)
        data class Dst(val name: String, val ranges: List<Range>)

        val map = HashMap<String, Dst>()
        input.drop(2).parts { p ->
            val (sn, dn) = Regex("(\\w+)-to-(\\w+) map:").matchEntire(p[0])!!.destructured
            val ranges = p.drop(1).map { s ->
                val (dst, src, len) = s.split(" ").map { it.toLong() }
                Range(dst, src, len)
            }
            map[sn] = Dst(dn, ranges)
        }
        data class I(val x1: Long, val x2: Long)

        fun convert(ranges: List<Range>, x1: Long, x2: Long): List<I> {
            val cvr = ArrayList<I>()
            val res = ArrayList<I>()
            for (r in ranges) {
                val y1 = maxOf(x1, r.src)
                val y2 = minOf(x2, r.src + r.len - 1)
                if (y1 <= y2) {
                    cvr.add(I(y1, y2))
                    res.add(I(y1 - r.src + r.dst, y2 - r.src + r.dst))
                }
            }
            cvr.sortBy { it.x1 }
            var cur = x1
            for ((y1, y2) in cvr) {
                if (y1 > cur) res.add(I(cur, y1 - 1))
                cur = y2 + 1
            }
            if (cur <= x2) res.add(I(cur, x2))
            return res
        }

        val result = seeds.chunked(2).minOf { seed ->
            var cn = "seed"
            val (x1, len) = seed
            var cur = ArrayList<I>()
            cur.add(I(x1, x1 + len - 1))
            while (true) {
                val dst = map[cn] ?: break
                cn = dst.name
                val nxt = ArrayList<I>()
                for (i in cur) {
                    val j = convert(dst.ranges, i.x1, i.x2)
                    nxt += j
                }
                cur = nxt
            }
            cur.minOf { it.x1 }
        }
        return result.toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

/**
 * Splits into space-separate parts of input and maps each part.
 */
fun <R> List<String>.parts(map: (List<String>) -> R): List<R> = buildList {
    var cur = ArrayList<String>()
    for (s in this@parts) {
        if (s == "") {
            add(map(cur))
            cur = ArrayList()
            continue
        }
        cur.add(s)
    }
    if (cur.isNotEmpty()) add(map(cur))
}
