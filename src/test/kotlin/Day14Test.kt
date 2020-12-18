import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

class Day14Test {
    val input = Paths.get("src/main/resources/day14Input").toFile().readLines(Charsets.UTF_8)

    val tinyInput = """
mask = 000000000000000000000000000000X1001X
mem[42] = 100
mask = 00000000000000000000000000000000X0XX
mem[26] = 1
    """.trimIndent().split("\n")

    @Test
    fun testTinyInput() {
        println(Day14(tinyInput).part1())
    }

    @Test
    fun testPart1() {
        println(Day14(input).part1())
    }

    @Test
    fun testTinyInput2() {
        println(Day14(tinyInput).part2())
    }


    @Test
    fun testPart2() {
        println(Day14(input).part2())
    }
}