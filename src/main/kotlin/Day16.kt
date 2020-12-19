class Day16(val input: List<String>) : DayN {

    private val ticketsAndTrains = parseInput()

    override fun part1(): Any {
        return ticketsAndTrains.nearbyTickets.flatMap { it.nums }
            .filter { ticketNum -> ticketsAndTrains.ticketRules.none { r -> r.isValid(ticketNum) } }
            .sum()
    }

    override fun part2(): Any {
        val possibilitiesByFieldForTicket: List<Map<Int, List<String>>> = ticketsAndTrains.nearbyTickets
            .filter { ticket -> ticket.nums.all { num -> ticketsAndTrains.ticketRules.any { it.isValid(num) } } }
            .map { ticket -> ticket.nums.map { getPossibilities(ticketsAndTrains.ticketRules, it) }.withIndex()
                    .map { (i, r) -> Pair(i, r.map { it.fieldName }) }.toMap()
            }

        val foundFieldsByNum = mutableMapOf<Int, String>()
        val allNums = ticketsAndTrains.yourTicket.nums.indices
        var numsLeftToFind = (allNums - foundFieldsByNum.keys)

        while (numsLeftToFind.isNotEmpty()) {
            numsLeftToFind.forEach { numToFind ->
                val possForNumForTicket: List<List<String>> = possibilitiesByFieldForTicket
                    .map { poss -> poss[numToFind]!!.filter { !foundFieldsByNum.containsValue(it) } }

                val poss = possForNumForTicket.flatten().distinct()
                    .filter { p -> possForNumForTicket.all { ticketPoss -> ticketPoss.contains(p) } }

                if (poss.size == 1) foundFieldsByNum[numToFind] = poss.first()
            }
            numsLeftToFind = (allNums - foundFieldsByNum.keys)
        }

        return ticketsAndTrains.yourTicket.nums.asSequence().withIndex()
            .map { (i, v) -> Pair(foundFieldsByNum[i]!!, v) }
            .filter { (fieldName, value) -> fieldName.startsWith("departure") }
            .map { (fieldName, value) -> value.toLong() }
            .reduce(Long::times)
    }

    private fun getPossibilities(ticketRules: List<TicketRule>, num: Int): List<TicketRule> {
        return ticketRules.filter { it.isValid(num) }
    }

    data class TicketsAndTrains(
        val ticketRules: List<TicketRule>,
        val yourTicket: Ticket,
        val nearbyTickets: List<Ticket>
    )

    data class TicketRule(val fieldName: String, val ranges: Pair<IntRange, IntRange>) {
        fun isValid(ticketNum: Int): Boolean {
            return ticketNum in ranges.first || ticketNum in ranges.second
        }
    }

    data class Ticket(val nums: List<Int>)

    private fun parseInput(): TicketsAndTrains {
        val rules = input.subList(0, input.indexOfFirst { it == "" }).map { line ->
            val (fieldName, start1, end1, start2, end2) = Regex("([^:]+): (\\d+)-(\\d+) or (\\d+)-(\\d+)")
                .matchEntire(line)!!.destructured
            TicketRule(fieldName, Pair(start1.toInt()..end1.toInt(), start2.toInt()..end2.toInt()))
        }

        val yourTicket = ticketFromLine(input[input.indexOfFirst { it == "your ticket:" } + 1])
        val otherTickets =
            input.subList(input.indexOfFirst { it == "nearby tickets:" } + 1, input.size).map(this::ticketFromLine)
        return TicketsAndTrains(rules, yourTicket, otherTickets)
    }

    private fun ticketFromLine(line: String): Ticket = Ticket(line.split(",").map { it.toInt() })

}