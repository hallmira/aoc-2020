class Day17(val input: List<String>) : DayN {
    private val grid = parseInput()

    data class Coordinate(val x: Int, val y: Int, val z: Int, val w: Int)

    data class ThreeDimGrid(val grid: MutableMap<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Boolean>>>>) {
        
        private fun getOrNull(c: Coordinate): Boolean? {
            return grid[c.w]?.get(c.z)?.get(c.y)?.get(c.x)
        }

        fun getNeighbors(x: Int, y: Int, z: Int, w: Int): List<Boolean> {
            return (-1..1).flatMap { wd ->
                (-1..1).flatMap { zd ->
                    (-1..1).flatMap { yd ->
                        (-1..1).map { xd -> Coordinate(xd, yd, zd, wd) }
                            .filter { it != Coordinate(0, 0, 0, 0) }
                            .map { c -> this.getOrNull(Coordinate(c.x + x, c.y + y, c.z + z, c.w + w)) ?: false }
                    }
                }
            }
        }

        fun deepCopy(): ThreeDimGrid {
            return ThreeDimGrid(this.grid.map { ws ->
                Pair(ws.key, ws.value.map { zs ->
                    Pair(zs.key, zs.value.map { ys ->
                        Pair(ys.key, ys.value.toMap().toMutableMap())
                    }.toMap().toMutableMap())
                }.toMap().toMutableMap())
            }.toMap().toMutableMap())
        }
    }

    private fun simulateCycle(gridAtStart: ThreeDimGrid): ThreeDimGrid {

        val expandedGrid = expandBy1(gridAtStart)

        return ThreeDimGrid(
            expandedGrid.grid.map { ws ->
                Pair(
                    ws.key, ws.value.map { zs ->
                        Pair(zs.key, zs.value.map { ys ->
                            Pair(ys.key, ys.value.map { xs ->
                                val activeNeighbors =
                                    gridAtStart.getNeighbors(xs.key, ys.key, zs.key, ws.key).filter { it }
                                val nextValue = if (xs.value) {
                                    activeNeighbors.size in 2..3
                                } else {
                                    activeNeighbors.size == 3
                                }
                                Pair(xs.key, nextValue)
                            }.toMap().toMutableMap())
                        }.toMap().toMutableMap())
                    }.toMap().toMutableMap()
                )
            }.toMap().toMutableMap()
        )
    }

    private fun expandBy1(gridAtStart: ThreeDimGrid): ThreeDimGrid {
        val expandedGrid = gridAtStart.deepCopy()

        val wmag = gridAtStart.grid.keys.maxOrNull()!! + 1
        val zmag = gridAtStart.grid[0]!!.keys.maxOrNull()!! + 1

        val ymagMin = gridAtStart.grid[0]!![0]!!.keys.minOrNull()!! - 1
        val ymagMax = gridAtStart.grid[0]!![0]!!.keys.maxOrNull()!! + 1

        val xmagMin = gridAtStart.grid[0]!![0]!![0]!!.keys.minOrNull()!! - 1
        val xmagMax = gridAtStart.grid[0]!![0]!![0]!!.keys.maxOrNull()!! + 1


        (-1 * wmag..wmag).forEach { w ->
            (-1 * zmag..zmag).forEach { z ->
                (ymagMin..ymagMax).forEach { y ->
                    (xmagMin..xmagMax).forEach { x ->
                        expandedGrid.grid.putIfAbsent(w, mutableMapOf())
                        expandedGrid.grid[w]!!.putIfAbsent(z, mutableMapOf())
                        expandedGrid.grid[w]!![z]!!.putIfAbsent(y, mutableMapOf())
                        expandedGrid.grid[w]!![z]!![y]!!.putIfAbsent(x, false)
                    }
                }
            }
        }
        return expandedGrid
    }

    override fun part1(): Any {
        return generateSequence(grid, this::simulateCycle)
            .take(7)
            .map { println("\nNEXT CYCLE\n${prettyPrint(it)}"); it }
            .last().grid.values.flatMap { zs -> zs.values.flatMap { ys -> ys.values.flatMap { xs -> xs.values } } }
            .filter { it }
            .count()
    }

    override fun part2(): Any {
        return part1()
    }

    fun parseInput(): ThreeDimGrid {
        val mag = (input.size - 1) / 2
        return ThreeDimGrid(
            mutableMapOf(0 to mutableMapOf(
                0 to input.map { line ->
                    line.map { it == '#' }.withIndex().map(this::toPair)
                        .map { Pair(it.first - mag, it.second) } // center around 0
                        .toMap().toMutableMap()
                }.withIndex().map(this::toPair)
                    .map { Pair(it.first - mag, it.second) } // center around 0
                    .toMap().toMutableMap()
            ))
        )
    }

    fun <T> toPair(i: IndexedValue<T>): Pair<Int, T> {
        return Pair(i.index, i.value)
    }

    /*
 example of produced:
     z=0 w= 0
 x:   -3 -2 -1  0  1  2  3  4
 y=-3  #  #  .  #  #  #  #  #
 y=-2  #  .  #  #  .  .  #  .
 y=-1  .  #  #  .  .  .  #  #
 y=0  #  #  #  .  #  .  .  .
 y=1  .  #  #  #  #  #  #  #
 y=2  #  #  .  .  .  .  #  #
 y=3  #  #  #  .  #  #  #  .
 y=4  .  #  .  #  .  #  .  .
  */
    fun prettyPrint(g: ThreeDimGrid) = g.grid.entries.sortedBy { it.key }.map { ws ->
        ws.value.entries.sortedBy { it.key }.map { zs ->
            getHeaderRow(zs, ws) +
                    zs.value.entries.sortedBy { it.key }.map { ys ->
                        "y=${ys.key} ".padStart(5) + ys.value.entries.sortedBy { it.key }.map { xs ->
                            if (xs.value) " # " else " . "
                        }.joinToString("")
                    }.joinToString("\n")
        }.joinToString("\n")
    }.joinToString("\n")

    private fun getHeaderRow(
        zs: MutableMap.MutableEntry<Int, MutableMap<Int, MutableMap<Int, Boolean>>>,
        ws: MutableMap.MutableEntry<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, Boolean>>>>
    ) = "\n    z=${zs.key} w= ${ws.key}\nx:  ${
        zs.value.entries.first().value.keys.sorted().map { it.toString().padStart(3) }.joinToString("")
    }\n"
}