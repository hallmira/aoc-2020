import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

class Day12Test {
    val input = Paths.get("src/main/resources/day12Input").toFile().readLines(Charsets.UTF_8)

    val tinyInput = """
        F10
        N3
        F7
        R90
        F11
    """.trimIndent().split("\n")

    @Test
    fun testTinyInput() {
        println(Day12(tinyInput).part1())
    }

    @Test
    fun testPart1() {
        println(Day12(input).part1())
    }

    @Test
    fun testTinyInput2() {
        println(Day12(tinyInput).part2())
    }


    @Test
    fun testPart2() {
        println(Day12(input).part2())
    }

    @Test
    fun testRotating(){
        var facing: Day12.RelativeLocation
        for (j in 1..3) {
            facing = Day12.RelativeLocation(2, 1)
            println("step size: $j")
            for (i in 0..12) {
                println(facing)
                facing = facing.rotate90Right(j)
            }

        }
    }
}