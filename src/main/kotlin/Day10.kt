class Day10(val input: List<String>) {
    private val adapterJoltages = input.map { it.toInt() }

    fun part1(): Int {
        // I wrote a lot more code for this part, but just freaking sorting them works :/
        val chain = listOf(0) + adapterJoltages.sorted() + (adapterJoltages.maxOrNull()!! + 3)
        val adapterByDifferences = chain.windowed(2).groupBy { (f, s) -> s - f }
        return adapterByDifferences[1]!!.size * adapterByDifferences[3]!!.size
    }

    fun part2(): Long {
        return allWaysToFillIn(listOf(0) + adapterJoltages.sorted() + (adapterJoltages.maxOrNull()!! + 3))
    }

    private fun allWaysToFillIn(unused: List<Int>): Long {
        val differences = unused.windowed(2).map { (first, second) -> second - first }

        return countContiguousOnes(differences).map { listSizeToCounts(it) }.reduce(Long::times)
    }

    /* get the length of each contiguous run of 1s
     * [3 1 1 1 3 3 1 3]  --> [3, 0, 1] */
    private fun countContiguousOnes(differences: List<Int>) =
        differences.fold(listOf(0), { soFar, next ->
            if (next == 1) {
                val last = soFar.last()
                soFar.dropLast(1).plus(last + 1)
            } else {
                soFar + 0
            }
        })

    /* for a string of ones `size` long, count how many valid combinations there are */
    private fun listSizeToCounts(size: Int): Long {
        /* given consecutive 3s, must pick them all --> 1 choice
           given one 1 surrounded by 3s, must pick it or will have a 3 + 3 difference --> 1 choice */
        return if (size < 2) {
            1
        } else {
            /* count ways to pick 0, 1, or 2 letters to drop. dropping any more results in too big of a difference */
            (0..2)
                .map { factorial(size - 1) / (factorial(it) * factorial(size - 1 - it)) }
                .reduce(Long::plus) // can pick 0 OR 1 OR 2 to drop
        }
    }

    private fun factorial(n: Int): Long {
        return LongRange(1, n.toLong()).fold(1, Long::times)
    }
}