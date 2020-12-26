class Day20(val input: List<String>) : DayN {

    override fun part1(): TileGrid {

        val parsed = parseInput()

        val tileGrid = TileGrid(buildEmptyGrid(parsed.size))
        return getAllPossibilities(tileGrid, parsed).first { it.isValid() }

    }

    private fun buildEmptyGrid(size: Int): Map<Position, Tile?> {
        val sideLength = Math.pow(size.toDouble(), .5).toInt()
        return (0 until sideLength).flatMap { col ->
            (0 until sideLength).map { row -> Pair(Position(row, col), null) }
        }.toMap()
    }

    override fun part2(): Any {
        return part1().toMegaTile().allOrientations().asSequence()
            .map { it.markSeaMonsters() }
            .first { o -> o.grid.any { it.contains('O') } }
            .grid
            .map { r -> r.count { it == '#' } }
    }

    fun getAllPossibilities(soFar: TileGrid, leftToMatch: List<Tile>): Sequence<TileGrid> {
        if (leftToMatch.size == 1) {
            // base case!
            leftToMatch.first().allOrientations().forEach { t ->
                val matched = soFar.tiles.toMutableMap()
                matched[matched.entries.filter { it.value == null }.first().key] = t
                val tileGrid = TileGrid(matched)
                if (tileGrid.isValid()) {
                    return sequenceOf(tileGrid)
                }
            }

            return sequenceOf()

        } else {
            val toMatch = soFar.tiles.entries.filter { it.value == null }.first().key
            return leftToMatch.flatMap { it.allOrientations() }.asSequence()
                .map { t -> Pair(t, TileGrid(soFar.tiles + Pair(toMatch, t))) }
                .filter { (t, g: TileGrid) -> g.isValid() }
                .flatMap { (t, g) -> getAllPossibilities(g, leftToMatch.filter { it.id != t.id }) }

        }
    }

    fun parseInput(): List<Tile> {
        return input.chunked(12)
            .map { l: List<String> ->
                Tile(
                    l[0].split("Tile", ":", " ")[2].toInt(),
                    Orientation.O,
                    l.slice(1..10).map { it.toCharArray().toList() }
                )
            }
    }

    /*
  0-------> col
  |   N     N
  | W * E W * E
  |   S     S
  row
     */

    enum class Side { NORTH, EAST, SOUTH, WEST }
    enum class Orientation { O, RL, UD, RLUD, T, TRL, TUD, TRLUD }

    /*
     [
      [....]
      [...]
     ]
     */
    data class Tile(val id: Int, val orientation: Orientation, val grid: List<List<Char>>) {

/* 3 r x 20c
                  #
#    ##    ##    ###
 #  #  #  #  #  #
 */
        private val seaMonsterRis = listOf(
            Pair(0, 18),
            Pair(1, 0), Pair(1, 5), Pair(1, 6), Pair(1, 11), Pair(1, 12), Pair(1, 17), Pair(1, 18), Pair(1, 19),
            Pair(2, 1), Pair(2, 4), Pair(2, 7), Pair(2, 10), Pair(2, 13), Pair(2, 16)
        )
        val lastRow = grid.size - 1
        val lastCol = grid.first().size - 1

        fun getSide(side: Side): List<Char> {
            return when (side) {
                Side.NORTH -> grid[0]
                Side.SOUTH -> grid[lastRow]
                Side.EAST -> grid.map { it[lastCol] }
                Side.WEST -> grid.map { it[0] }
            }
        }

        fun allOrientations(): List<Tile> {
            val transposed: List<List<Char>> =
                grid.withIndex().flatMap { r -> r.value.withIndex().map { c -> Triple(c.index, r.index, c.value) } }
                    .groupBy { it.first }.map { r -> r.value.map { it.third } }
            return listOf(
                Pair(Orientation.O, grid),
                Pair(Orientation.UD, grid.reversed()),
                Pair(Orientation.RL, grid.map { it.reversed() }),
                Pair(Orientation.RLUD, grid.map { it.reversed() }.reversed()),
                Pair(Orientation.T, transposed),
                Pair(Orientation.TUD, transposed.reversed()),
                Pair(Orientation.TRL, transposed.map { it.reversed() }),
                Pair(Orientation.TRLUD, transposed.map { it.reversed() }.reversed())
            ).map { Tile(id, it.first, it.second) }
        }


        fun markSeaMonsters(): Tile {
            val newGrid = grid.map { it.toMutableList() }.toMutableList()

            grid.indices.windowed(3).forEach { ris ->
                grid[0].indices.windowed(20).forEach { cis ->
                    if (isSeaMonster(grid.slice(ris).map { it.slice(cis) })) {
                        seaMonsterRis.map { (r, c) -> newGrid[ris.first() + r][cis.first() + c] = 'O' }
                    }
                }
            }

            return this.copy(grid = newGrid)
        }

        private fun isSeaMonster(chunk: List<List<Char>>): Boolean {
            return seaMonsterRis.all { (r, c) -> chunk[r][c] == '#' }
        }
    }

    data class Position(val rowNum: Int, val colNum: Int)

    data class TileGrid(val tiles: Map<Position, Tile?>) {
        fun getNeighborSides(p: Position, t: Tile): Map<List<Char>, List<Char>?> {

            return mapOf(
                t.getSide(Side.NORTH) to tiles[p.copy(rowNum = p.rowNum - 1)]?.getSide(Side.SOUTH),
                t.getSide(Side.SOUTH) to tiles[p.copy(rowNum = p.rowNum + 1)]?.getSide(Side.NORTH),
                t.getSide(Side.WEST) to tiles[p.copy(colNum = p.colNum - 1)]?.getSide(Side.EAST),
                t.getSide(Side.EAST) to tiles[p.copy(colNum = p.colNum + 1)]?.getSide(Side.WEST),
            )
        }

        fun matches(l1: List<Char>, l2: List<Char>): Boolean {
            return l1.size == l2.size && l1.zip(l2).all { (f, s) -> f == s }
        }

        fun isValid(): Boolean {
            return tiles.filter { it.value != null }.map { getNeighborSides(it.key, it.value!!) }
                .all { m ->
                    m.filter { (tSide, nSide) -> nSide != null }
                        .all { (tSide, nSide) -> matches(tSide, nSide!!) }
                }
        }

        fun toMegaTile(): Tile {
            val borderless = tiles.map { (p, t) -> Pair(p, dropBorder(t!!)) }.toMap()
            return Tile(0, Orientation.O,
                (0 until 12).flatMap { br ->
                    (0 until 8).map { lr ->
                        (0 until 12).flatMap { bc ->
                            (0 until 8).map { lc ->
                                borderless[Position(br, bc)]!!.grid[lr][lc]
                            }
                        }
                    }
                })
        }

        private fun dropBorder(t: Tile): Tile {
            return t.copy(grid = t.grid.slice(1 until t.lastRow).map { it.slice(1 until t.lastCol) })
        }
    }
}