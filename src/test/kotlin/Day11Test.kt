import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

class Day11Test {
    val input = Paths.get("src/main/resources/day11Input").toFile().readLines(Charsets.UTF_8)

    val tinyInput = """
#.##.##.##
#######.##
#.#.#..#..
####.##.##
#.##.##.##
#.#####.##
..#.#.....
##########
#.######.#
#.#####.##
    """.trimIndent().split("\n")

    @Test
    fun testTinyInput() {
        println(Day11(tinyInput).part1())
    }

    @Test
    fun testPart1() {
        println(Day11(input).part1())
    }

    @Test
    fun testTinyInput2() {
        println(Day11(tinyInput).part2())
    }


    @Test
    fun testPart2() {
        println(Day11(input).part2())
    }
}