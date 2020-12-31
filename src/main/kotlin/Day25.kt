class Day25(val input: List<String>) : DayN {
    val globalCryptoDivisor = 20201227L
    val globalSubjectNumber = 7L

    val cardPublicKey = input.first().toLong()
    val doorPublicKey = input[1].toLong()

    override fun part1(): Any {
        return loopResult(findLoopSize(cardPublicKey), doorPublicKey)
    }

    private fun findLoopSize(publicKey: Long): Long {
        var loopSize = 1L
        var loopResult = loopResult(loopSize, globalSubjectNumber)

        while (loopResult != publicKey) {
            loopSize += 1
            loopResult = loopResult * 7 % globalCryptoDivisor
        }
        return loopSize
    }

    private fun loopResult(loopSize: Long, subjectNumber: Long): Long {
        var value = 1L
        for (i in 1..loopSize) {
            value = (value * subjectNumber) % globalCryptoDivisor
        }
        return value
    }

    override fun part2(): Any {
        TODO("Not yet implemented")
    }

}