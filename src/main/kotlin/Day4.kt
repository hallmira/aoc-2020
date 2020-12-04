class Day4 {

    private val requiredFields = PassportField.values().filter { it.required }

    fun part1(input: List<String>): Int {
        return inputToPassports(input).count { it.keys.containsAll(requiredFields) }
    }

    fun part2(input: List<String>): Int {
        return inputToPassports(input)
            .count { it.keys.containsAll(requiredFields) && it.all { (field, value) -> field.validator.invoke(value) } }
    }

    private fun inputToPassports(input: List<String>): List<Map<PassportField, String>> {
        return input.joinToString("\n").split("\n\n").map { it.replace("\n", " ") }
            .map { passport ->
                passport.split(" ")
                    .map {
                        val (fieldName, value) = it.split(":")
                        Pair(PassportField.valueOf(fieldName), value)
                    }.toMap()
            }
    }

    enum class PassportField(val validator: (String) -> Boolean, val required: Boolean = true) {
        byr({ s -> Integer.valueOf(s) in 1920..2002 }),
        iyr({ s -> Integer.valueOf(s) in 2010..2020 }),
        eyr({ s -> Integer.valueOf(s) in 2020..2030 }),
        hgt({ s ->
            when {
                s.endsWith("cm") -> Integer.valueOf(s.removeSuffix("cm")) in 150..193
                s.endsWith("in") -> Integer.valueOf(s.removeSuffix("in")) in 59..76
                else -> false
            }
        }),
        hcl({ s ->
            when {
                s.startsWith("#") -> s.removePrefix("#").matches(Regex("[0-9a-f]{6}"))
                else -> false
            }
        }),
        ecl({ s -> s in EYE_COLORS }),
        pid({ s -> s.matches(Regex("[0-9]{9}")) }),
        cid({ true }, false)
    }

    companion object {
        val EYE_COLORS = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
    }
}