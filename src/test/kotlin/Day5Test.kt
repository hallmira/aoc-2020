import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

typealias row = Int
typealias col = Int
typealias seatId = Int

class Day5Test {
    val input = Paths.get("src/main/resources/day5Input").toFile().readLines(Charsets.UTF_8)

    val tinyINput1 = """
    BFFFBBFRRR
    FFFBBBFRRR
    BBFFBBFRLL
    """.trimIndent().split("\n")

    val tinyOutput1: List<Triple<row, col, seatId>> = listOf(
        Triple(70, 7, 567),
        Triple(14, 7, 119),
        Triple(102, 4, 820)
    )

    @Test
    fun test1() {
//        assertEquals(tinyOutput1, Day5(tinyINput1).part1().map { Triple(it.row, it.col, it.seatId) })
        println(Day5(input).part1().maxByOrNull { it.seatId }!!.seatId)

    }

    @Test
    fun test2() {
        println(Day5(input).part2())
    }

}