class Day22(val input: List<String>) : DayN {
    val parsed = parseInput()
    val gameCache = mutableMapOf<GameState, Player>()
    override fun part1(): Any {
        var state = parsed

        while (state.winner() == null) {
            state = playRound(state)
        }

        return calculateScore(state)
    }

    override fun part2(): Any {
        val state = playRecursiveGame(GameHistory(listOf(parsed), 0))
        return calculateScore(state.history.last())
    }

    private fun calculateScore(state: GameState) = state.winner()!!.second.cards.reversed().withIndex()
        .map { it.value * (it.index + 1) }
        .reduce(Int::plus)

    // first "on top"
    data class CardStack(val cards: List<Int>)
    data class GameState(val p1: CardStack, val p2: CardStack, val overrideWinner: Pair<Player, CardStack>? = null) {
        fun winner() = when {
            overrideWinner != null -> overrideWinner
            p1.cards.isEmpty() -> Pair(Player.P2, p2)
            p2.cards.isEmpty() -> Pair(Player.P1, p1)
            else -> null
        }
    }

    enum class Player { P1, P2 }
    data class GameHistory(val history: List<GameState>, val recursionLevel: Int)

    fun parseInput(): GameState {
        val splitIndex = input.indexOfFirst { it.isBlank() }
        return GameState(
            toCardStack(input.slice(0 until splitIndex)),
            toCardStack(input.slice(splitIndex + 1 until input.size))
        )
    }

    private fun toCardStack(slice: List<String>): CardStack {
        return CardStack(slice.drop(1).map { it.toInt() })
    }

    fun playRound(gameState: GameState): GameState {
        val p1Card = gameState.p1.cards.first()
        val p2Card = gameState.p2.cards.first()

        return if (p1Card > p2Card) {
            gameState.copy(
                p1 = CardStack(gameState.p1.cards.drop(1) + p1Card + p2Card),
                p2 = CardStack(gameState.p2.cards.drop(1))
            )
        } else {
            gameState.copy(
                p1 = CardStack(gameState.p1.cards.drop(1)),
                p2 = CardStack(gameState.p2.cards.drop(1) + p2Card + p1Card)
            )
        }
    }

    fun playRecursiveGame(gameHistory: GameHistory): GameHistory {
        var state = gameHistory
        while (state.history.last().winner() == null) {
            state = playRecursiveRound(state)
        }
        return state
    }

    fun playRecursiveRound(gameHistory: GameHistory): GameHistory {
        val currentState = gameHistory.history.last()
      if (currentState in gameHistory.history.dropLast(1)) {
            // base case!
           return GameHistory(
                gameHistory.history + currentState.copy(overrideWinner = Pair(Player.P1, currentState.p1)),
                gameHistory.recursionLevel
            )
        } else {

            val p1Card = currentState.p1.cards.first()
            val p2Card = currentState.p2.cards.first()

            val p1DeckAfterDraw = currentState.p1.cards.drop(1)
            val p2DeckAfterDraw = currentState.p2.cards.drop(1)

            val p1Win = GameState(
                CardStack(p1DeckAfterDraw + p1Card + p2Card),
                CardStack(p2DeckAfterDraw)
            )
            val p2Win = GameState(
                CardStack(p1DeckAfterDraw),
                CardStack(p2DeckAfterDraw + p2Card + p1Card)
            )

            val realNextState: GameState =
                getNextState(currentState, p1Win, p2Win, p1Card, p1DeckAfterDraw, p2Card, p2DeckAfterDraw, gameHistory)

           return GameHistory(gameHistory.history + realNextState, gameHistory.recursionLevel)
        }
    }

    private fun getNextState(
        currentState: GameState,
        p1Win: GameState,
        p2Win: GameState,
        p1Card: Int,
        p1DeckAfterDraw: List<Int>,
        p2Card: Int,
        p2DeckAfterDraw: List<Int>,
        gameHistory: GameHistory
    ): GameState {
        if (currentState in gameCache) {
            return when (gameCache[currentState]) {
                Player.P1 -> p1Win
                Player.P2 -> p2Win
                else -> throw RuntimeException()
            }
        } else if (canRecurse(p1Card, p1DeckAfterDraw) && canRecurse(p2Card, p2DeckAfterDraw)) {
            val recursiveWinner = playRecursiveGame(
                GameHistory(
                    listOf(
                        GameState(CardStack(p1DeckAfterDraw.take(p1Card)), CardStack(p2DeckAfterDraw.take(p2Card)))
                    ), gameHistory.recursionLevel + 1
                )
            ).history.last().winner()!!.first
            gameCache[currentState] = recursiveWinner
            return when (recursiveWinner) {
                Player.P1 -> p1Win
                Player.P2 -> p2Win
            }
        } else {
            return if (p1Card > p2Card) p1Win else p2Win
        }
    }

    private fun canRecurse(p1Card: Int, drop: List<Int>): Boolean {
        return drop.size >= p1Card
    }
}