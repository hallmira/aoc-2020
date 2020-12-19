class Day15(val input: List<String>) {

    fun part1(): Int {
        return getNumberSpokenAtTurn(2020)
    }

    fun part2(): Int {
        return getNumberSpokenAtTurn(30000000)
    }

    data class TurnState(
        val turnNum: Int,
        val numsByTurnsLastUsed: MutableMap<Int, Int>, // does not include spoken
        val spoken: Int
    )

    private fun getNumberSpokenAtTurn(finalTurnNum: Int): Int {
        val startingNumbers = parseInput()
        val startAsMap = startingNumbers.withIndex().map { (i, v) -> Pair(v, i + 1) }.toMap().toMutableMap()

        return generateSequence(TurnState(startAsMap.size, startAsMap, startingNumbers.last()),
            { prevState ->
                val lastSpokenTurn = prevState.numsByTurnsLastUsed[prevState.spoken]
                val speakThisTurn = lastSpokenTurn?.let { prevState.turnNum - lastSpokenTurn } ?: 0
                prevState.numsByTurnsLastUsed[prevState.spoken] = prevState.turnNum

                TurnState(prevState.turnNum + 1, prevState.numsByTurnsLastUsed, speakThisTurn)
            })
            .take(finalTurnNum - startingNumbers.size + 1).last().spoken
    }

    private fun parseInput(): List<Int> {
        return input.first().split(",").map { it.toInt() }
    }
}