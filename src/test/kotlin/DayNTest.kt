import org.junit.jupiter.api.Test
import java.nio.file.Paths

class DayNTest {
    val n = 15
    val maker: (List<String>) -> DayN = { input : List<String> -> Day15(input)}
    val input =  Paths.get("src/main/resources/day${n}Input").toFile().readLines(Charsets.UTF_8)
    val tinyInput = maker.invoke(input).tinyInput()
    fun tinyInputToInput(tiny: String) = tiny.trimIndent().split("\n")

    @Test
    fun testTinyInput() {
        println(maker.invoke(tinyInputToInput(tinyInput)).part1())
    }

    @Test
    fun testPart1() {
        println(maker.invoke(input).part1())
    }

    @Test
    fun testTinyInput2() {
        println(maker.invoke(tinyInputToInput(tinyInput)).part1())
    }


    @Test
    fun testPart2() {
        println(maker.invoke(input).part1())
    }
}