import org.junit.jupiter.api.Test
import java.nio.file.Paths

class DayNTest {
    val n = 25
    val maker: (List<String>) -> DayN = { input: List<String> -> Day25(input) }
    val input = Paths.get("src/main/resources/day${n}Input").toFile().readLines(Charsets.UTF_8)

    val tinyInput =
        """
5764801
17807724
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