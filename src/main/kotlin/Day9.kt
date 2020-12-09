import kotlin.math.abs

class Day9(private val input: List<String>) {
    private val longs = input.map { it.toLong() }

    fun part1(): Long {
        return getSummersFromWindowSize(25)
    }

    fun part2(): Long {
        val sliceThatAdds = getSliceThatAdds(part1())
        return sliceThatAdds.minOrNull()!! + sliceThatAdds.maxOrNull()!!
    }

    private fun getSummersFromWindowSize(windowSize: Int) = longs.windowed(windowSize + 1)
        .asSequence()
        .map { window ->
            Pair(
                findTwoThatAdd(window.slice(0 until windowSize), window[windowSize]),
                window[windowSize]
            )
        }.first { it.first == null }.second

    private fun findTwoThatAdd(summers: List<Long>, summee: Long): Pair<Long, Long>? {
        return summers.map { Pair(it, abs(summee - it)) }
            .firstOrNull { (it, partner) -> summers.contains(partner) }
    }

    private fun getSliceThatAdds(summee: Long): List<Long> {
        return IntRange(2, longs.size)
            .asSequence()
            .flatMap { windowSize -> longs.windowed(windowSize, partialWindows = false) }
            .first { window -> window.sum() == summee }
    }
}

