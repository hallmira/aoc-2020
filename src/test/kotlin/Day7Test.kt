import org.junit.jupiter.api.Test
import java.nio.file.Paths

class Day7Test {
    val input = Paths.get("src/main/resources/day7Input").toFile().readLines(Charsets.UTF_8)

    val tinyInput = """
        light red bags contain 1 bright white bag, 2 muted yellow bags.
        dark orange bags contain 3 bright white bags, 4 muted yellow bags.
        bright white bags contain 1 shiny gold bag.
        muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
        shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
        dark olive bags contain 3 faded blue bags, 4 dotted black bags.
        vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
        faded blue bags contain no other bags.
        dotted black bags contain no other bags.
    """.trimIndent().split("\n")

    val tinyInput2 = """
shiny gold bags contain 2 dark red bags.
dark red bags contain 2 dark orange bags.
dark orange bags contain 2 dark yellow bags.
dark yellow bags contain 2 dark green bags.
dark green bags contain 2 dark blue bags.
dark blue bags contain 2 dark violet bags.
dark violet bags contain no other bags."""
        .trimIndent().split("\n")

    @Test
    fun testTinyInput() {
        println(Day7(tinyInput).part1())
    }

    @Test
    fun testPart1() {
        println(Day7(input).part1())
    }

    @Test
    fun testTinyInput2() {
        println(Day7(tinyInput2).part2())
    }

    @Test
    fun testPart2() {
        println(Day7(input).part2())
    }
}