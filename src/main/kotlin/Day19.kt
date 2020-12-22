import java.lang.Integer.max
import java.lang.Integer.min
import java.lang.RuntimeException

/*
--- Day 19: Monster Messages ---

You land in an airport surrounded by dense forest. As you walk to your high-speed train,
the Elves at the Mythical Information Bureau contact you again.
They think their satellite has collected an image of a sea monster!
 Unfortunately, the connection to the satellite is having problems,
 and many of the messages sent back from the satellite have been corrupted.

They sent you a list of the rules valid messages should obey and a list of received messages they've collected so far
 (your puzzle input).

The rules for valid messages (the top part of your puzzle input) are numbered and build upon each other. For example:

0: 1 2
1: "a"
2: 1 3 | 3 1
3: "b"

Some rules, like 3: "b", simply match a single character (in this case, b).

The remaining rules list the sub-rules that must be followed;
for example, the rule 0: 1 2 means that to match rule 0, the text being checked must match rule 1,
and the text after the part that matched rule 1 must then match rule 2.

Some of the rules have multiple lists of sub-rules separated by a pipe (|).
 This means that at least one list of sub-rules must match.
 (The ones that match might be different each time the rule is encountered.)
 For example, the rule 2: 1 3 | 3 1 means that to match rule 2,
 the text being checked must match rule 1 followed by rule 3 or it must match rule 3 followed by rule 1.

Fortunately, there are no loops in the rules, so the list of possible matches will be finite.
Since rule 1 matches a and rule 3 matches b, rule 2 matches either ab or ba. Therefore, rule 0 matches aab or aba.

Here's a more interesting example:

0: 4 1 5
1: 2 3 | 3 2
2: 4 4 | 5 5
3: 4 5 | 5 4
4: "a"
5: "b"

Here, because rule 4 matches a and rule 5 matches b, rule 2 matches two letters that are the same (aa or bb),
and rule 3 matches two letters that are different (ab or ba).

Since rule 1 matches rules 2 and 3 once each in either order, it must match two pairs of letters,
one pair with matching letters and one pair with different letters.
This leaves eight possibilities: aaab, aaba, bbab, bbba, abaa, abbb, baaa, or babb.

Rule 0, therefore, matches a (rule 4), then any of the eight options from rule 1, then b (rule 5):
aaaabb, aaabab, abbabb, abbbab, aabaab, aabbbb, abaaab, or ababbb.

The received messages (the bottom part of your puzzle input) need to be checked against the rules so you can
determine which are valid and which are corrupted.
 Including the rules and the messages together, this might look like:

0: 4 1 5
1: 2 3 | 3 2
2: 4 4 | 5 5
3: 4 5 | 5 4
4: "a"
5: "b"

ababbb
bababa
abbbab
aaabbb
aaaabbb

Your goal is to determine the number of messages that completely match rule 0.
 In the above example, ababbb and abbbab match, but bababa, aaabbb, and aaaabbb do not, producing the answer 2.
  The whole message must match all of rule 0; there can't be extra unmatched characters in the message.
  (For example, aaaabbb might appear to match rule 0 above, but it has an extra unmatched b on the end.)

How many messages completely match rule 0?

--- Part Two ---

As you look over the list of messages, you realize your matching rules aren't quite right. To fix them, completely replace rules 8: 42 and 11: 42 31 with the following:

8: 42 | 42 8
11: 42 31 | 42 11 31

This small change has a big impact: now, the rules do contain loops, and the list of messages they could hypothetically match is infinite. You'll need to determine how these changes affect which messages are valid.

Fortunately, many of the rules are unaffected by this change; it might help to start by looking at which rules always match the same set of values and how those rules (especially rules 42 and 31) are used by the new versions of rules 8 and 11.

(Remember, you only need to handle the rules you have; building a solution that could handle any hypothetical combination of rules would be significantly more difficult.)

For example:

42: 9 14 | 10 1
9: 14 27 | 1 26
10: 23 14 | 28 1
1: "a"
11: 42 31
5: 1 14 | 15 1
19: 14 1 | 14 14
12: 24 14 | 19 1
16: 15 1 | 14 14
31: 14 17 | 1 13
6: 14 14 | 1 14
2: 1 24 | 14 4
0: 8 11
13: 14 3 | 1 12
15: 1 | 14
17: 14 2 | 1 7
23: 25 1 | 22 14
28: 16 1
4: 1 1
20: 14 14 | 1 15
3: 5 14 | 16 1
27: 1 6 | 14 18
14: "b"
21: 14 1 | 1 14
25: 1 1 | 1 14
22: 14 14
8: 42
26: 14 22 | 1 20
18: 15 15
7: 14 5 | 1 21
24: 14 1

abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa
bbabbbbaabaabba
babbbbaabbbbbabbbbbbaabaaabaaa
aaabbbbbbaaaabaababaabababbabaaabbababababaaa
bbbbbbbaaaabbbbaaabbabaaa
bbbababbbbaaaaaaaabbababaaababaabab
ababaaaaaabaaab
ababaaaaabbbaba
baabbaaaabbaaaababbaababb
abbbbabbbbaaaababbbbbbaaaababb
aaaaabbaabaaaaababaa
aaaabbaaaabbaaa
aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
babaaabbbaaabaababbaabababaaab
aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba

Without updating rules 8 and 11, these rules only match three messages: bbabbbbaabaabba, ababaaaaaabaaab, and ababaaaaabbbaba.

However, after updating rules 8 and 11, a total of 12 messages match:

    bbabbbbaabaabba
    babbbbaabbbbbabbbbbbaabaaabaaa
    aaabbbbbbaaaabaababaabababbabaaabbababababaaa
    bbbbbbbaaaabbbbaaabbabaaa
    bbbababbbbaaaaaaaabbababaaababaabab
    ababaaaaaabaaab
    ababaaaaabbbaba
    baabbaaaabbaaaababbaababb
    abbbbabbbbaaaababbbbbbaaaababb
    aaaaabbaabaaaaababaa
    aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
    aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba

After updating rules 8 and 11, how many messages completely match rule 0?

 */
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



