class Day21(val input: List<String>) : DayN {
    val parsed = input.map { line ->
        val (i, a) = line.split("(contains ", ")").filter { it.isNotBlank() }
        Food(i.split(" ").filter{it.isNotBlank()}, a.split(", "))
    }
    override fun part1(): Any {
        val allergensToForSureIngredient: Map<String, String> = getAllergensByIngredient()
        return parsed.flatMap { it.ingredients }.filter { it !in allergensToForSureIngredient.values }.count()
    }
    
    override fun part2(): Any {
        val allergensToForSureIngredient: Map<String, String> = getAllergensByIngredient()
        return allergensToForSureIngredient.entries.sortedBy { it.key }.map { it.value }.joinToString(",")
    }

    private fun getAllergensByIngredient(): Map<String, String> {
        val allergens = parsed.flatMap { it.allergens }.distinct()

        val allergensToPossibleIngredients = allergens
            .map { a -> Pair(a, parsed.filter { it.allergens.contains(a) }.map { it.ingredients }) }
            .map { (a, lists) -> Pair(a, lists.flatten().distinct().filter { i -> lists.all { it.contains(i) } }) }
            .toMap().toMutableMap()

        val allergensToForSureIngredient: MutableMap<String, String?> = allergens.associateWith { null }.toMutableMap()
        var allergensToFind = allergensToForSureIngredient.filterValues { it == null }.keys
        while (allergensToFind.isNotEmpty()) {
            allergensToFind.forEach { a ->
                val left = allergensToPossibleIngredients[a]!!.filter { !allergensToForSureIngredient.values.contains(it) }
                if (left.size == 1) {
                    allergensToForSureIngredient[a] = left.first()
                } else {
                    allergensToPossibleIngredients[a] = left
                }
            }
            allergensToFind = allergensToForSureIngredient.filterValues { it == null }.keys
        }
        return allergensToForSureIngredient.entries.map { Pair(it.key, it.value!!) }.toMap()
    }


    data class Food(val ingredients: List<String>, val allergens: List<String>)



}