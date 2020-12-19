import org.junit.jupiter.api.Test
import java.nio.file.Paths

class Day15Test {
    val input = """
0,1,5,10,3,12,19
    """.trimIndent().split("\n")

    val tinyInput = """
0,3,6
    """.trimIndent().split("\n")

    @Test
    fun testTinyInput() {
        println(Day15(tinyInput).part1())
    }

    @Test
    fun testPart1() {
        println(Day15(input).part1())
    }

    @Test
    fun testTinyInput2() {
        println(Day15(tinyInput).part2())
    }


    @Test
    fun testPart2() {
        println(Day15(input).part2())
    }
}