package com.github.mvukic.day13

import com.github.mvukic.linesForDay

private const val PRIZE_OFFSET = 10_000_000_000_000L

fun main() {
    val lines = linesForDay(13)
    val machines = lines.filter { it.isNotBlank() }.chunked(3).map(::parseClawMachine)
    val costs = machines.mapNotNull { it.cheapestWin() }
    println("prizes won: ${costs.size} of ${machines.size}")
    println(costs.sum())
}

private data class ClawMachine(val ax: Long, val ay: Long, val bx: Long, val by: Long, val px: Long, val py: Long)

private fun parseClawMachine(block: List<String>): ClawMachine {
    val n = Regex("""\d+""").findAll(block.joinToString(" ")).map { it.value.toLong() }.toList()
    return ClawMachine(n[0], n[1], n[2], n[3], n[4] + PRIZE_OFFSET, n[5] + PRIZE_OFFSET)
}

/**
 * Solves a*A + b*B = P with Cramer's rule; there is no press limit any more. With non-collinear
 * buttons (true for every machine in the input) the solution is unique, hence also the cheapest.
 * Intermediate products are around 1e15, well within Long.
 */
private fun ClawMachine.cheapestWin(): Long? {
    val det = ax * by - ay * bx
    if (det == 0L) return null
    val aNum = px * by - py * bx
    val bNum = ax * py - ay * px
    if (aNum % det != 0L || bNum % det != 0L) return null
    val a = aNum / det
    val b = bNum / det
    if (a < 0 || b < 0) return null
    return 3 * a + b
}
