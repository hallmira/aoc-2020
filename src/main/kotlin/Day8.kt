class Day8(private val input: List<String>) {
    private val instructions = parseInput()

    fun part1(): Int {
        return goUntilLoopedOrDone(instructions).finalAcc
    }

    fun part2(): Int {
        return instructions.withIndex().asSequence()
            .filter { (i, inst) -> inst is Jump || inst is Nop }
            .map { (i, inst) ->
                val new = if (inst is Jump) Nop(inst.num) else Jump(inst.num)
                goUntilLoopedOrDone(replaceAt(i, new, instructions))
            }.first { it.exitedSuccessfully }.finalAcc
    }

    private fun goUntilLoopedOrDone(input: List<Instruction>): LoopResult {
        val instructionsAndStatuses = input.map { Pair(it, false) }.toTypedArray()

        var globalAcc = 0
        var nextIndex = 0
        var nextInstruction: Instruction

        while (nextIndex in instructionsAndStatuses.indices && !instructionsAndStatuses[nextIndex].second) {

            instructionsAndStatuses[nextIndex] = Pair(instructionsAndStatuses[nextIndex].first, true)
            nextInstruction = instructionsAndStatuses[nextIndex].first

            nextIndex = when (nextInstruction) {
                is Acc -> {
                    globalAcc += nextInstruction.num
                    nextIndex + 1
                }
                is Jump -> nextIndex + nextInstruction.num
                else -> nextIndex + 1
            }
        }

        return LoopResult(nextIndex == instructionsAndStatuses.size, globalAcc)
    }

    abstract class Instruction(val num: Int)

    class Jump(num: Int) : Instruction(num)
    class Acc(num: Int) : Instruction(num)
    class Nop(num: Int) : Instruction(num)

    class LoopResult(val exitedSuccessfully: Boolean, val finalAcc: Int)

    private fun parseInput(): List<Instruction> {
        return input.map {
            val (name, num) = it.split(" ")
            val numNum = num.toInt()
            when (name) {
                "nop" -> Nop(numNum)
                "jmp" -> Jump(numNum)
                else -> Acc(numNum)
            }
        }
    }

    private fun replaceAt(index: Int, newInstruction: Instruction, input: List<Instruction>): List<Instruction> {
        val newInput = input.toMutableList()
        newInput[index] = newInstruction
        return newInput.toList()
    }
}

