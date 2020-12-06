class Day6(private val input: List<String>) {

    fun part1(): Int {
        return getGroupAnswers().map { g -> g.people.flatMap { it.answersYes }.distinct().count() }.sum()
    }

    fun part2(): Int {
        return getGroupAnswers()
            .map { it.getAllQuestionsYes().count() }
            .sum()
    }

    private fun getGroupAnswers(): List<GroupAnswers> {
        return input.joinToString("\n").split("\n\n")
            .map { g -> GroupAnswers(g.split("\n").map { Person(it.toList()) }) }
    }

    data class Person(val answersYes: List<Char>) {
        fun answeredYes(question: Char): Boolean {
            return answersYes.contains(question)
        }
    }

    data class GroupAnswers(val people: List<Person>) {

        fun getAllQuestionsYes(): List<Char> {
            val allQuestions = people.flatMap { person -> person.answersYes }.distinct()
            return allQuestions.filter { a -> people.all { it.answeredYes(a) } }
        }
    }
}
