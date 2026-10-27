package com.github.mvukic.day17

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(17)
    val registers = lines.take(3).map { it.substringAfter(": ").toLong() }
    val program = lines.first { it.startsWith("Program:") }.substringAfter(": ").split(",").map { it.trim().toInt() }

    val a = findA(program, registers[1], registers[2], 0L, program.size - 1) ?: error("No value of A found")
    check(execute(longArrayOf(a, registers[1], registers[2]), program) == program)
    println(a)
}

// The program loops, shifting A right by 3 bits per iteration until it is 0, and each output
// depends only on the current A. So A is built 3 bits at a time from the last output backwards:
// each candidate must reproduce the program's suffix from `index` on. Trying the low bits in
// ascending order makes the first full match the lowest A.
private fun findA(program: List<Int>, b: Long, c: Long, prefix: Long, index: Int): Long? {
    if (index < 0) return prefix
    for (bits in 0L..7L) {
        val a = prefix * 8 + bits
        if (a == 0L) continue
        if (execute(longArrayOf(a, b, c), program) == program.subList(index, program.size)) {
            findA(program, b, c, a, index - 1)?.let { return it }
        }
    }
    return null
}

// Runs the program, mutating registers [A, B, C], and returns the output values.
private fun execute(reg: LongArray, program: List<Int>): List<Int> {
    val output = mutableListOf<Int>()
    var ip = 0
    while (ip + 1 < program.size) {
        val operand = program[ip + 1]
        val combo = {
            when (operand) {
                in 0..3 -> operand.toLong()
                4 -> reg[0]
                5 -> reg[1]
                6 -> reg[2]
                else -> error("Invalid combo operand $operand")
            }
        }
        when (program[ip]) {
            0 -> reg[0] = shiftDivide(reg[0], combo())
            1 -> reg[1] = reg[1] xor operand.toLong()
            2 -> reg[1] = combo() and 7
            3 -> if (reg[0] != 0L) { ip = operand; continue }
            4 -> reg[1] = reg[1] xor reg[2]
            5 -> output += (combo() and 7).toInt()
            6 -> reg[1] = shiftDivide(reg[0], combo())
            7 -> reg[2] = shiftDivide(reg[0], combo())
        }
        ip += 2
    }
    return output
}

// A / 2^power, truncated; Kotlin's shr would wrap shift amounts of 64 or more.
private fun shiftDivide(a: Long, power: Long): Long = if (power >= 63) 0 else a shr power.toInt()
