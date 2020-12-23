import java.lang.Integer.max
import java.lang.Integer.min
import java.lang.RuntimeException

class Day19(val input: List<String>) : DayN {

    override fun part1(): Any {
        return messages
            .sortedBy { it.length }
            .map { Pair(it, rule.matches(it)) }
            .filter { m -> m.second }
            .count()
    }

    override fun part2(): Any {
        return part1()
    }

    private val split = input.indexOfFirst { it.isBlank() }
    private val messages: List<String> = input.slice(split + 1 until input.size)

    private val rawRules: Map<Int, Any> = input.subList(0, split)
        .map { line ->
            val (num, rule) = line.split(": ")
            Pair(num.toInt(), when {
                rule.startsWith("\"") -> rule[1]
                rule.contains("|") -> {
                    val rules = rule.split(" | ").map { r -> r.split(" ").map { it.toInt() } }
                    Pair(rules.first(), rules.last())
                }
                else -> rule.split(" ").map { it.toInt() }
            })
        }.toMap()
    private val rulesCache = mutableMapOf<Int, Rule>()
    private val rule = getRule(0)

    private fun getRule(num: Int): Rule {
        return rulesCache[num] ?: run {
            val rule = makeRule(num, rawRules[num]!!)
            rulesCache[num] = rule
            rule
        }
    }

    private fun makeRule(num: Int, body: Any): Rule {

        if (num == 0) {
            return RepeatingMidFixRule(getRule(42), getRule(31), 811)
        }

        return when (body) {
            is Char -> LetterRule(body, num)
            is List<*> -> OrderRule(body.map { getRule(it as Int) }, num)
            is Pair<*, *> -> {
                val p = body as Pair<List<Int>, List<Int>>
                OrRule(Pair(makeRule(num, p.first), makeRule(num, p.second)), num)
            }
            else -> throw RuntimeException()
        }
    }

    interface Rule {
        val num: Int
        fun matches(message: String): Boolean
        val matchMin: Int
        val matchMax: Int?
    }

    data class LetterRule(val char: Char, override val num: Int) : Rule {
        override fun matches(message: String): Boolean = message.length == 1 && message[0] == char
        override val matchMin = 1
        override val matchMax = 1
    }

    data class OrRule(val rules: Pair<Rule, Rule>, override val num: Int) : Rule {
        override val matchMin: Int = min(rules.first.matchMin, rules.second.matchMin)
        override val matchMax: Int? = if (rules.first.matchMax == null || rules.second.matchMax == null) null else {
            max(rules.first.matchMax!!, rules.second.matchMax!!)
        }

        override fun matches(message: String): Boolean = rules.first.matches(message) || rules.second.matches(message)
    }

    data class OrderRule(val inOrder: List<Rule>, override val num: Int) : Rule {
        override val matchMin: Int = inOrder.map { it.matchMin }.sum()
        override val matchMax: Int? = if (inOrder.none { it.matchMax == null }) {
            inOrder.map { it.matchMax!! }.sum()
        } else null

        override fun matches(message: String): Boolean {
            var unmatched = message
            inOrder.forEach { rule ->

                val match = IntRange(1, unmatched.length).toList().reversed().asSequence()
                    .filter { l -> l >= rule.matchMin && (rule.matchMax?.let { l <= it } ?: true) }
                    .map { windowLength -> unmatched.slice(0 until windowLength) }
                    .firstOrNull { window -> rule.matches(window) }

                if (match == null) {
                    return false
                } else {
                    unmatched = unmatched.drop(match.length)
                }
            }
            return unmatched.isBlank()
        }
    }

    data class RepeatingMidFixRule(val prefix: Rule, val suffix: Rule, override val num: Int) : Rule {

        // matches 42* 42 42 [42 31]* 31
        // or 41{n+1} 31{n}
        override fun matches(message: String): Boolean {
            val matchesList = matchRepeatedly(message, suffix)
            if (matchesList.isEmpty()) {
                // no suffix match!
                return false
            }

            return matchesList
                .filter { (count, match) -> match.length != message.length } // can't match the whole thing! need 1 prefix
                .flatMap { (count, match) ->
                    matchRepeatedly(message.dropLast(match.length), prefix)
                        .filter { (pcount, pmatch) -> pcount >= (count + 1) }
                        .map { (pcount, pmatch) -> pmatch + match } // combine prefix + suffix match for full match
                        .map { fullmatch -> Pair(fullmatch, message.dropLast(fullmatch.length)) }
                        .filter { (fullmatch, leftover) -> leftover.isBlank() }
                }.isNotEmpty()
        }

        private fun matchRepeatedly(message: String, rule: Rule): List<Pair<Int, String>> {
            if (message.length < rule.matchMin) {
                return emptyList() // too short!
            }

            val firstMatches = IntRange(0, message.length).asSequence()
                .filter { s -> (message.length - s) >= rule.matchMin }
                .map { startingIndex -> message.slice(startingIndex until message.length) }
                .filter { window -> rule.matches(window) }.toList()

            if (firstMatches.isEmpty()) {
                // couldn't match once! boo!
                return emptyList()
            }

            return firstMatches
                .map { m -> Pair(m, matchRepeatedly(message.dropLast(m.length), rule)) }
                .filter { (firstMatch, restMatches) -> restMatches.isNotEmpty() }
                .flatMap { (firstMatch, restMatches) ->
                    restMatches.map { (matchCount, match) -> Pair(matchCount + 1, match + firstMatch) }
                } + firstMatches.map { Pair(1, it) }
        }

        override val matchMin: Int = prefix.matchMin
        override val matchMax: Int? = null
    }


}



