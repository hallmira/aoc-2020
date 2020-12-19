import org.junit.jupiter.api.Test
import java.nio.file.Paths

class DayNTest {
    val n = 16
    val maker: (List<String>) -> DayN = { input: List<String> -> Day16(input) }
    val input = Paths.get("src/main/resources/day${n}Input").toFile().readLines(Charsets.UTF_8)
    val tinyInput =
        """
class: 0-1 or 4-19
row: 0-5 or 8-19
seat: 0-13 or 16-19

your ticket:
11,12,13

nearby tickets:
3,9,18
15,1,5
5,14,9
        """.trimIndent().split("\n")

    @Test
    fun testTinyInput() {
        println(maker.invoke(tinyInput).part1())
    }

    @Test
    fun testPart1() {
        println(maker.invoke(input).part1())
    }

    @Test
    fun testTinyInput2() {
        println(maker.invoke(tinyInput).part2())
    }

    @Test
    fun testPart2() {
        println(maker.invoke(input).part2())
    }
}