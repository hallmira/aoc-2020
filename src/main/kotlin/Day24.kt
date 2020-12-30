import kotlin.math.abs

class Day24(val input: List<String>) : DayN {

    override fun part1(): Any {
        return executeInstructions(
            TileGrid(mutableMapOf(Position(0, 0) to TileState.WHITE)),
            parseInput()
        ).tiles.entries.count { it.value == TileState.BLACK }
    }

    override fun part2(): Any {
        val grid = TileGrid(mutableMapOf(Position(0, 0) to TileState.WHITE))

        executeInstructions(grid, parseInput())
        (1..10).forEach { dailyFlip(grid) }

        return grid.tiles.entries.count { it.value == TileState.BLACK }
    }

    private fun dailyFlip(grid: TileGrid) {
        val newGrid = grid.tiles.toMap().toMutableMap()
        val xs = grid.tiles.entries.filter { it.value == TileState.BLACK }.map { it.key.x }
        val ys = grid.tiles.entries.filter { it.value == TileState.BLACK }.map { it.key.y }

        (ys.min()!! - 2..ys.max()!! + 3).flatMap { y ->
            (xs.min()!! - 3..xs.max()!! + 3).map { x ->
                Position(x, y)
            }
        }.filter { (x, y) -> (abs(y) % 2 == 0 && abs(x) % 2 == 0) || (abs(y) % 2 == 1 && abs(x) % 2 == 1) }
            .forEach { pos ->
                val tile = grid.tiles[pos] ?: TileState.WHITE
                val blackNeighbors: Int = grid.getNeighbors(pos).count { it == TileState.BLACK }
                val newTile: TileState = when {
                    tile == TileState.BLACK && (blackNeighbors == 0 || blackNeighbors > 2) -> {
                        TileState.WHITE
                    }
                    tile == TileState.WHITE && blackNeighbors == 2 -> TileState.BLACK
                    else -> tile
                }
                newGrid[pos] = newTile
            }

        newGrid.forEach { (p, t) -> grid.tiles[p] = t }

    }

    enum class TileState {
        BLACK, WHITE;

        fun flip(): TileState {
            return if (this == BLACK) WHITE else BLACK
        }
    }

    enum class InstructionStep {
        e, se, sw, w, nw, ne
    }

    data class Position(val x: Int, val y: Int)

    /* (x, y)


      (1, -1)  (1, 1)
         nw      ne

(-2,0)  w  (0,0)  e (2, 0)

          sw    se
      (-1,-1)  (-1,1)
     */
    data class TileGrid(val tiles: MutableMap<Position, TileState>) {

        private val deltas: List<Pair<Int, Int>> = listOf(
            Pair(1, 1),
            Pair(2, 0),
            Pair(-1, 1),
            Pair(-1, -1),
            Pair(-2, 0),
            Pair(1, -1)
        )

        fun flipState(position: Position) {
            tiles[position] = tiles[position]?.flip() ?: TileState.BLACK
        }

        fun getNeighbors(pos: Position): List<TileState> {
            return deltas
                .map { (xd, yd) -> Position(x = pos.x + xd, y = pos.y + yd) }
                .map { tiles[it] ?: TileState.WHITE }
        }
    }

    fun executeInstructions(grid: TileGrid, instructionsList: List<List<InstructionStep>>): TileGrid {
        var current: Position
        instructionsList.forEach { instructions ->
            current = Position(0, 0)
            instructions.forEach {
                current = executeStep(current, it)
            }
            grid.flipState(current)
        }
        return grid
    }

    private fun executeStep(position: Position, instruction: InstructionStep): Position {
        return when (instruction) {
            InstructionStep.e -> position.copy(x = position.x + 2)
            InstructionStep.w -> position.copy(x = position.x - 2)

            InstructionStep.ne -> position.copy(x = position.x + 1, y = position.y + 1)
            InstructionStep.sw -> position.copy(x = position.x - 1, y = position.y - 1)

            InstructionStep.nw -> position.copy(x = position.x - 1, y = position.y + 1)
            InstructionStep.se -> position.copy(x = position.x + 1, y = position.y - 1)
        }
    }

    fun parseInput(): List<List<InstructionStep>> {
        return input.map { it.toCharArray().toList() }.map(this::lineToInstructionSteps).toList()
    }

    fun lineToInstructionSteps(line: List<Char>): List<InstructionStep> {
        var i = 0
        val instructions = mutableListOf<InstructionStep>()

        while (i < line.size) {
            val current = line[i]
            if (current == 'e' || current == 'w') {
                i += 1
                instructions.add(InstructionStep.valueOf(current.toString()))
            } else {
                val next = line[i + 1]
                i += 2
                instructions.add(InstructionStep.valueOf("$current$next"))
            }
        }
        return instructions.toList()
    }
}