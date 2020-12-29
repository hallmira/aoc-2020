import org.junit.jupiter.api.Test
import java.nio.file.Paths

class DayNTest {
    val n = 22
    val maker: (List<String>) -> DayN = { input: List<String> -> Day22(input) }
    val input = Paths.get("src/main/resources/day${n}Input").toFile().readLines(Charsets.UTF_8)

    val tinyInput =
        """
Player 1:
43
19

Player 2:
2
29
14
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