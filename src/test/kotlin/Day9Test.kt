import org.junit.jupiter.api.Test
import java.nio.file.Paths

class Day9Test {
    val input = Paths.get("src/main/resources/day9Input").toFile().readLines(Charsets.UTF_8)

    val tinyInput = """
        35
        20
        15
        25
        47
        40
        62
        55
        65
        95
        102
        117
        150
        182
        127
        219
        299
        277
        309
        576
    """.trimIndent().split("\n")

    @Test
    fun testTinyInput() {
        println(Day9(tinyInput).part1())
    }

    @Test
    fun testPart1() {
        println(Day9(input).part1())
    }

    @Test
    fun testTinyInput2() {
        println(Day9(tinyInput).part2())
    }

    @Test
    fun testPart2() {
        println(Day9(input).part2())
    }
}