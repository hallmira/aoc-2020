import org.junit.jupiter.api.Test
import java.nio.file.Paths

class Day10Test {
    val input = Paths.get("src/main/resources/day10Input").toFile().readLines(Charsets.UTF_8)

    val tinyInput = """
16
10
15
5
1
11
7
19
6
12
4
    """.trimIndent().split("\n")

    val lessTinyInput = """
28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3
    """.trimIndent().split("\n")

    @Test
    fun testTinyInput() {
        println(Day10(tinyInput).part1())
    }

    @Test
    fun testPart1() {
        println(Day10(input).part1())
    }

    @Test
    fun testTinyInput2() {
        println(Day10(tinyInput).part2())
    }

    @Test
    fun testLessTinyInput2() {
        println(Day10(lessTinyInput).part2())
    }


    @Test
    fun testPart2() {
        println(Day10(input).part2())
    }
}