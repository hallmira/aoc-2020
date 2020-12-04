import org.junit.jupiter.api.Test
import java.nio.file.Paths

class Day4Test {
    val input = Paths.get("src/main/resources/day4Input").toFile().readLines(Charsets.UTF_8)

    @Test
    fun test1(){
        println(Day4().part1(input))
    }

    @Test
    fun test2(){
        println(Day4().part2(input))
    }

}