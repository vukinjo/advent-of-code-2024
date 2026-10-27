package com.github.mvukic.day17

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(17)
    val registers = lines.take(3).map { it.substringAfter(": ").toLong() }.toLongArray()
    val program = lines.first { it.startsWith("Program:") }.substringAfter(": ").split(",").map { it.trim().toInt() }

    val output = run(registers, program)
    println("Registers after halt: A=${registers[0]} B=${registers[1]} C=${registers[2]}")
    println(output.joinToString(","))
}

// Runs the program, mutating registers [A, B, C], and returns the output values.
private fun run(reg: LongArray, program: List<Int>): List<Int> {
    val output = mutableListOf<Int>()
    var ip = 0
    while (ip + 1 < program.size) {
        val opcode = program[ip]
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
        when (opcode) {
            0 -> reg[0] = divide(reg[0], combo())
            1 -> reg[1] = reg[1] xor operand.toLong()
            2 -> reg[1] = combo() and 7
            3 -> if (reg[0] != 0L) { ip = operand; continue }
            4 -> reg[1] = reg[1] xor reg[2]
            5 -> output += (combo() and 7).toInt()
            6 -> reg[1] = divide(reg[0], combo())
            7 -> reg[2] = divide(reg[0], combo())
        }
        ip += 2
    }
    return output
}

// A / 2^power, truncated; Kotlin's shr would wrap shift amounts of 64 or more.
private fun divide(a: Long, power: Long): Long = if (power >= 63) 0 else a shr power.toInt()
