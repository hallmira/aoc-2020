class Day3(input: List<CharArray>) {
    private val biome = Biome(input)

    fun part1(): Int {
        return getPathToBottom(Location(0, 0), listOf(), 1, 3).second.count { it == '#' }
    }

    fun part2(): Long {
        return listOf(
            Pair(1, 1),
            Pair(1, 3),
            Pair(1, 5),
            Pair(1, 7),
            Pair(2, 1)
        ).map {
            getPathToBottom(Location(0, 0), listOf(), it.first, it.second).second.count { c -> c == '#' }.toLong()
        }.reduce(Long::times)
    }

    private fun getPathToBottom(
        current: Location,
        soFar: List<Char>,
        xMove: Int,
        yMove: Int
    ): Pair<Location, List<Char>> {
        val nextLocation = current.copy(x = current.x + xMove, y = current.y + yMove)
        val nextChar = biome.getCharAt(nextLocation)
        return if (nextChar == null) {
            // base case!
            Pair(nextLocation, soFar)
        } else {
            getPathToBottom(nextLocation, soFar + nextChar, xMove, yMove)
        }
    }

    data class Location(val x: Int, val y: Int)
    class Biome(basePattern: List<CharArray>) {

        /*  x down
         *  y right
         */
        private var currentPattern: List<CharArray> = basePattern

        private fun expandPattern() {
            currentPattern = currentPattern.map { it + it }
        }

        fun getCharAt(location: Location): Char? {
            return when (val row = currentPattern.getOrNull(location.x)) {
                null -> {
                    // bottom! success!
                    null
                }
                else -> {
                    when (val column = row.getOrNull(location.y)) {
                        null -> {
                            expandPattern()
                            currentPattern[location.x][location.y]
                        }
                        else -> column
                    }
                }
            }
        }
    }
}