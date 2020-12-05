class Day5(private val input: List<String>) {

    data class Seat(val row: Int, val col: Int) {
        val seatId: Int
            get() = row * 8 + col
    }

    fun part1(): List<Seat> {
        return input.map(this::lineToSeat)
    }

    fun part2(): Int {
        val occupiedSeats = part1()
        val occupiedSeatIds = occupiedSeats.map { it.seatId }

        return (generateAllSeatIds() - occupiedSeats)

// the below roles are technically correct, but aren't necessary to narrow it down to 1 seat!
//            .groupBy { it.row }
//            .filterNot { it.value.map { s -> s.col }.containsAll((0..7).toList()) } // exclude fully empty rows
//            .values.flatten()

            .map { it.seatId } // to list o ids
            .first { occupiedSeatIds.contains(it + 1) && occupiedSeatIds.contains(it - 1) } // require occupied neighbors
    }

    private fun lineToSeat(line: String): Seat {
        return Seat(toRow(line.slice(0..6)), toCol(line.slice(7..9)))
    }

    private fun generateAllSeatIds(): List<Seat> {
        return IntRange(0, 127).flatMap { row -> IntRange(0, 7).map { col -> Seat(row, col) } }
    }

    private fun toRow(s: String): Int = Integer.parseInt(s.map { if (it == 'F') '0' else '1' }.joinToString(""), 2)
    private fun toCol(s: String): Int = Integer.parseInt(s.map { if (it == 'L') '0' else '1' }.joinToString(""), 2)

}