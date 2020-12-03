import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

internal class Day3Test {
    val input = Paths.get("src/main/resources/day3Input").toFile().readLines(Charsets.UTF_8).map { it.toCharArray() }

    @Test
    fun miniTest(){
        val mininput =
            """
                ..#......###....#...##..#.#....
                .#.#.....#.##.....###...##...##
                ..#.#..#...........#.#..#......
            """.trimIndent().split("\n").map { it.toCharArray() }
        assertEquals( 1, Day3(mininput).part1())
    }

    @Test
    fun part1() {
        println(Day3(input).part1())
    }
    @Test
    fun part2() {
        println(Day3(input).part2())
    }
}