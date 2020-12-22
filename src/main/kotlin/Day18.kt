/*
--- Day 18: Operation Order ---

As you look out the window and notice a heavily-forested continent slowly appear over the horizon,
 you are interrupted by the child sitting next to you. They're curious if you could help them with their
  math homework.

Unfortunately, it seems like this "math" follows different rules than you remember.

The homework (your puzzle input) consists of a series of expressions that consist of
addition (+), multiplication (*), and parentheses ((...)).
Just like normal math, parentheses indicate that the expression inside must be evaluated
before it can be used by the surrounding expression.
 Addition still finds the sum of the numbers on both sides of the operator, and
 multiplication still finds the product.

However, the rules of operator precedence have changed. Rather than evaluating multiplication before addition,
the operators have the same precedence, and are evaluated left-to-right regardless of the order in which they appear.

For example, the steps to evaluate the expression 1 + 2 * 3 + 4 * 5 + 6 are as follows:

1 + 2 * 3 + 4 * 5 + 6
  3   * 3 + 4 * 5 + 6
      9   + 4 * 5 + 6
         13   * 5 + 6
             65   + 6
                 71

Parentheses can override this order; for example, here is what happens if parentheses are added to form 1 + (2 * 3) + (4 * (5 + 6)):

1 + (2 * 3) + (4 * (5 + 6))
1 +    6    + (4 * (5 + 6))
     7      + (4 * (5 + 6))
     7      + (4 *   11   )
     7      +     44
            51

Here are a few more examples:

    2 * 3 + (4 * 5) becomes 26.
    5 + (8 * 3 + 9 + 3 * 4 * 3) becomes 437.
    5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)) becomes 12240.
    ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 becomes 13632.

Before you can help with the homework, you need to understand it yourself.
Evaluate the expression on each line of the homework; what is the sum of the resulting values?

--- Part Two ---

You manage to answer the child's questions and they finish part 1 of their homework,
but get stuck when they reach the next section: advanced math.

Now, addition and multiplication have different precedence levels,
 but they're not the ones you're familiar with. Instead, addition is evaluated before multiplication.

For example, the steps to evaluate the expression 1 + 2 * 3 + 4 * 5 + 6 are now as follows:

1 + 2 * 3 + 4 * 5 + 6
  3   * 3 + 4 * 5 + 6
  3   *   7   * 5 + 6
  3   *   7   *  11
     21       *  11
         231

Here are the other examples from above:

    1 + (2 * 3) + (4 * (5 + 6)) still becomes 51.
    2 * 3 + (4 * 5) becomes 46.
    5 + (8 * 3 + 9 + 3 * 4 * 3) becomes 1445.
    5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)) becomes 669060.
    ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 becomes 23340.

What do you get if you add up the results of evaluating the homework problems using these new rules?


 */

class Day18(val input: List<String>) : DayN {
    override fun part1(): Any {
        return input.map(this::lineToAdvancedExpressions)
            .map { evaluate(it, this::pickFirstOperand) }
            .reduce(Long::plus)
    }

    override fun part2(): Any {
        return input.map(this::lineToAdvancedExpressions)
            .map { evaluate(it, this::pickPlusThenMultiply) }
            .reduce(Long::plus)
    }

    private fun evaluate(
        advExpr: List<Any>,
        pickOperand: (List<List<IndexedValue<Any>>>) -> List<IndexedValue<Any>>
    ): Long {
        var soFar = advExpr
        while (soFar.size > 3) {
            val (beforeEval, toEval, afterEval) = if (soFar.contains("(")) {
                getRightMostParenSnip(soFar)
            } else {
                getLeftMostOperandSnip(soFar, pickOperand)
            }
            soFar = beforeEval + evaluate(toEval, pickOperand) + afterEval
        }

        return (soFar[1] as Operand).preform(soFar[0] as Long, soFar[2] as Long)
    }

    private fun getLeftMostOperandSnip(
        soFar: List<Any>,
        pickOperand: (List<List<IndexedValue<Any>>>) -> List<IndexedValue<Any>>
    ): Triple<List<Any>, List<Any>, List<Any>> {
        val windowToEvaluate = pickOperand.invoke(soFar.withIndex().windowed(size = 3))

        val before = if (windowToEvaluate[0].index == 0) listOf() else {
            soFar.slice(0 until windowToEvaluate[0].index)
        }
        val after = if (windowToEvaluate[2].index == soFar.indices.last()) listOf() else {
            soFar.slice(windowToEvaluate[2].index + 1 until soFar.size)
        }
        return Triple(before, windowToEvaluate.map { it.value }, after)

    }

    private fun pickFirstOperand(l: List<List<IndexedValue<Any>>>): List<IndexedValue<Any>> {
        return l.first { it[1].value is Operand }
    }

    private fun pickPlusThenMultiply(l: List<List<IndexedValue<Any>>>): List<IndexedValue<Any>> {
        return l.firstOrNull { it[1].value == Operand.PLUS } ?: l.first { it[1].value == Operand.MULTIPLY }
    }

    private fun getRightMostParenSnip(soFar: List<Any>): Triple<List<Any>, List<Any>, List<Any>> {
        val startParen = soFar.withIndex().last { it.value == "(" }
        val endParen = soFar.withIndex().first { it.index > startParen.index && it.value == ")" }

        val before = if (startParen.index == 0) listOf() else soFar.slice(0 until startParen.index)
        val after =
            if (endParen.index == soFar.indices.last()) listOf() else soFar.slice(endParen.index + 1 until soFar.size)

        return Triple(before, soFar.slice((startParen.index + 1) until endParen.index), after)
    }

    private fun lineToAdvancedExpressions(line: String): List<Any> {
        return line
            .replace("(", " ( ")
            .replace(")", " ) ")
            .split(" ")
            .filter { it.isNotEmpty() }
            .map {
                when {
                    Regex("^[0-9]+$").matches(it) -> it.toLong()
                    it == "+" || it == "*" -> Operand.fromChar(it[0])
                    else -> it
                }
            }
    }

    enum class Operand(val char: Char, val preform: (Long, Long) -> Long) {
        PLUS('+', Long::plus),
        MULTIPLY('*', Long::times);

        companion object {
            fun fromChar(char: Char): Operand {
                return values().first { it.char == char }
            }
        }
    }
}