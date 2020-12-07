import java.lang.IllegalArgumentException

class Day7(private val input: List<String>) {

    private val bagsToRules = parseInput()

    fun part1(): Int {
        return bagsToRules.keys.map { getCanRecursivelyContain(it, Bag("shiny", "gold")) }.count { it }
    }

    fun part2(): Int {
        return getCountMustContain(Bag("shiny", "gold"))
    }

    private fun getCountMustContain(container: Bag): Int {
        val containerRules = bagsToRules[container] ?: throw IllegalArgumentException("cannot find rules for $container")
        return when {
            containerRules.any { it is NoBagRule } -> 0 // base case! must contain no bags
            else -> {
                containerRules.map { rule: BagRule ->
                    (rule as NumColorBagRule).allowedBagNum +  // count the bag itself
                            getCountMustContain(rule.allowedBag) * rule.allowedBagNum // and all it must contain
                }.sum()
            }
        }
    }

    private fun getCanRecursivelyContain(container: Bag, containee: Bag): Boolean {
        val containerRules = bagsToRules[container] ?: throw IllegalArgumentException("cannot find rules for $container")
        return when {
            containerRules.any { it.canDirectlyContain(containee, 1) } -> true // base case!
            containerRules.any { it is NoBagRule } -> false // base case! no hope for containing :(
            else -> {
                containerRules.any { rule: BagRule -> getCanRecursivelyContain((rule as NumColorBagRule).allowedBag, containee) }
            }
        }
    }

    data class Bag(val adjective: String, val color: String)
    interface BagRule {
        fun canDirectlyContain(bag: Bag, num: Int): Boolean
    }

    data class NumColorBagRule(val allowedBagNum: Int, val allowedBag: Bag) : BagRule {
        override fun canDirectlyContain(bag: Bag, num: Int): Boolean {
            return bag == allowedBag && num <= allowedBagNum
        }
    }

    class NoBagRule : BagRule {
        override fun canDirectlyContain(bag: Bag, num: Int) = false
    }

    private fun parseInput(): Map<Bag, List<BagRule>> {
        return input.map { fullLine ->
            val (bagSubject, bagRules) = fullLine.split(" bags contain ")
            val (adjective, color) = bagSubject.split(" ")
            Pair(Bag(adjective, color), bagRules.split(", ").map { toBagRule(it) })
        }.toMap()
    }

    private fun toBagRule(strRule: String): BagRule {
        return when (strRule) {
            "no other bags." -> NoBagRule()
            else -> {
                val (num, adj, color) = strRule.removeSuffix(".").removeSuffix("s").removeSuffix(" bag").split(" ")
                NumColorBagRule(num.toInt(), Bag(adj, color))
            }
        }
    }
}

