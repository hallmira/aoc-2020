import java.lang.RuntimeException
import kotlin.math.abs

/*
(E, S)

         (0, -1)
            N
(-1, 0) W (0,0) E (1, 0)
            S
          (0, 1)
*/


class Day12(val input: List<String>) {
    private val instructions = parseInput()

    fun part1(): Int {
        var current = Location(0, 0, RelativeLocation(1, 0))
        instructions.forEach { current = it.nextLocation(current) }
        return abs(current.east) + abs(current.south)
    }


    fun part2(): Int {
        var current = Location(0, 0, RelativeLocation(10, -1))
        instructions.forEach { current = it.nextLocationWithWaypoint(current) }
        return abs(current.east) + abs(current.south)
    }

    private fun parseInput() = input.map {
        Instruction(it.toCharArray()[0], it.slice(1 until it.length).toInt())
    }

    data class RelativeLocation(val east: Int, val south: Int) {
        private fun rotate90Right(): RelativeLocation = RelativeLocation(-1 * south, east)
        private fun rotate90Left(): RelativeLocation = RelativeLocation(south, east * -1)

        fun rotate90Right(times: Int): RelativeLocation {
            return generateSequence(this, {r -> r.rotate90Right()}).take(times + 1).last()
        }

        fun rotate90Left(times: Int): RelativeLocation {
            return generateSequence(this, {r -> r.rotate90Left()}).take(times + 1).last()
        }
    }

    data class Location(val east: Int, val south: Int, val wayPoint: RelativeLocation) {
        fun forwardTowardsWaypoint(num: Int): Location {
            return this.copy(east = this.east + (wayPoint.east * num), south = this.south + (wayPoint.south * num))
        }
    }

    data class Instruction(val letter: Char, val num: Int) {
        fun nextLocation(current: Location): Location {
            return when (letter) {
                'N' -> current.copy(south = current.south + num * -1)
                'S' -> current.copy(south = current.south + num)
                'E' -> current.copy(east = current.east + num)
                'W' -> current.copy(east = current.east + num * -1)
                'R' -> current.copy(wayPoint = current.wayPoint.rotate90Right(num / 90))
                'L' -> current.copy(wayPoint = current.wayPoint.rotate90Left(num / 90))
                'F' -> current.forwardTowardsWaypoint(num)
                else -> throw RuntimeException()
            }
        }

        fun nextLocationWithWaypoint(location: Location): Location {
            return when (letter) {
                'N' -> location.copy(wayPoint = location.wayPoint.copy(south = location.wayPoint.south + num * -1))
                'S' -> location.copy(wayPoint = location.wayPoint.copy(south = location.wayPoint.south + num))
                'E' -> location.copy(wayPoint = location.wayPoint.copy(east = location.wayPoint.east + num))
                'W' -> location.copy(wayPoint = location.wayPoint.copy(east = location.wayPoint.east + num * -1))
                'R' -> location.copy(wayPoint = location.wayPoint.rotate90Right(num / 90))
                'L' -> location.copy(wayPoint = location.wayPoint.rotate90Left(num / 90))
                'F' -> location.forwardTowardsWaypoint(num)
                else -> throw RuntimeException()
            }
        }
    }
}