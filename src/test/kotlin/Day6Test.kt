import org.junit.jupiter.api.Test
import java.nio.file.Paths

class Day6Test {
    val input = Paths.get("src/main/resources/day6Input").toFile().readLines(Charsets.UTF_8)

    val tinyInput = """
        abc

        a
        b
        c

        ab
        ac

        a
        a
        a
        a

        b
    """.trimIndent().split("\n")

    @Test
    fun testTinyInput() {
        println(Day6(tinyInput).part1())
    }

    @Test
    fun testPart1() {
        println(Day6(input).part1())
    }

    @Test
    fun testTinyInput2() {
        println(Day6(tinyInput).part2())
    }
    @Test
    fun testPart2() {
        println(Day6(input).part2())

    }
}