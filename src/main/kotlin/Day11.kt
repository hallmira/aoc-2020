import java.lang.IllegalArgumentException

class Day11(private val inputs: List<String>) {
    fun part1(): Int {
        return occupiedAfterLastMovement { g -> nextTurn(g, 4) { s, rIndex, cIndex -> s.getNeighbors(rIndex, cIndex) } }
    }

    fun part2(): Int {
        return occupiedAfterLastMovement { g -> nextTurn(g, 5) { s, rIndex, cIndex -> s.getVisibles(rIndex, cIndex) } }
    }

    private fun occupiedAfterLastMovement(nextTurnFunction: (SeatGrid) -> SeatGrid): Int {
        var lastSeatGrid = parseInput()
        var nextSeatGrid = nextTurnFunction.invoke(lastSeatGrid)
        while (lastSeatGrid != nextSeatGrid) {
            lastSeatGrid = nextSeatGrid
            nextSeatGrid = nextTurnFunction.invoke(lastSeatGrid)
        }

        return nextSeatGrid.seats.map { row -> row.filter { it is Seat && it.occupied }.count() }.sum()
    }

    fun nextTurn(
        seatGrid: SeatGrid,
        threshold: Int,
        relevantSeatExtractor: (SeatGrid, Int, Int) -> List<FloorUnit>
    ): SeatGrid {
        return seatGrid.copy(
            seats = seatGrid.seats.mapIndexed { rIndex, row ->
                row.mapIndexed { cIndex, floorUnit ->
                    val neighbors = relevantSeatExtractor.invoke(seatGrid, rIndex, cIndex).filterIsInstance<Seat>()
                    if (floorUnit is Seat && floorUnit.occupied &&
                        (neighbors.filter { it.occupied }.count() >= threshold)
                    ) {
                        Seat(false)
                    } else if (floorUnit is Seat && !floorUnit.occupied && (neighbors.none { it.occupied })) {
                        Seat(true)
                    } else {
                        floorUnit
                    }
                }
            })
    }

    data class SeatGrid(val seats: List<List<FloorUnit>>) {
        fun getNeighbors(row: Int, col: Int): List<FloorUnit> {
            return IntRange(-1, 1).flatMap { row -> IntRange(-1, 1).map { col -> Pair(row, col) } }
                .filter { it != Pair(0, 0) }
                .map { (r, c) -> seats.getOrNull(r + row)?.getOrNull(col + c) }
                .filterNotNull()
        }

        fun getVisibles(r: Int, c: Int): List<FloorUnit> {
            return IntRange(-1, 1).flatMap { rowDir -> IntRange(-1, 1).map { colDir -> Pair(rowDir, colDir) } }
                .filter { it != Pair(0, 0) }
                .map { (rowDir, colDir) ->
                    var magnitude = 1
                    var nextUnit = seats.getOrNull(r + (rowDir * magnitude))?.getOrNull(c + (colDir * magnitude))
                    while (nextUnit is Floor) {
                        magnitude += 1
                        nextUnit = seats.getOrNull(r + (rowDir * magnitude))?.getOrNull(c + (colDir * magnitude))
                    }
                    nextUnit
                }.filterNotNull()
        }
    }

    interface FloorUnit
    data class Seat(val occupied: Boolean) : FloorUnit
    class Floor : FloorUnit

    private fun parseInput(): SeatGrid {
        return SeatGrid(inputs.map { row ->
            row.map {
                when (it) {
                    '.' -> Floor()
                    '#' -> Seat(true)
                    'L' -> Seat(false)
                    else -> throw IllegalArgumentException()
                }
            }
        })
    }
}