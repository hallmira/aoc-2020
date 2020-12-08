import org.junit.jupiter.api.Test
import java.nio.file.Paths

class Day8Test {
    val input = Paths.get("src/main/resources/day8Input").toFile().readLines(Charsets.UTF_8)

    val tinyInput = """
        nop +0
        acc +1
        jmp +4
        acc +3
        jmp -3
        acc -99
        acc +1
        jmp -4
        acc +6
    """.trimIndent().split("\n")

    @Test
    fun testTinyInput() {
        println(Day8(tinyInput).part1())
    }

    @Test
    fun testPart1() {
        println(Day8(input).part1())
    }

    @Test
    fun testTinyInput2() {
        println(Day8(tinyInput).part2())
    }

    @Test
    fun testPart2() {
        println(Day8(input).part2())
    }
}