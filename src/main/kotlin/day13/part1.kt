package com.github.mvukic.day13

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(13)
    val machines = lines.filter { it.isNotBlank() }.chunked(3).map(::parseMachine)
    println(machines.sumOf { it.cheapestWin() ?: 0L })
}

private data class Machine(val ax: Long, val ay: Long, val bx: Long, val by: Long, val px: Long, val py: Long)

private val numberRegex = Regex("""\d+""")

private fun parseMachine(block: List<String>): Machine {
    val (ax, ay, bx, by, px, py) = block.joinToString(" ").let { text ->
        numberRegex.findAll(text).map { it.value.toLong() }.toList()
    }
    return Machine(ax, ay, bx, by, px, py)
}

private operator fun <T> List<T>.component6() = this[5]

/**
 * Solves a*A + b*B = P with Cramer's rule. Returns the token cost, or null if there is no
 * non-negative integer solution within the 100-press limit.
 */
private fun Machine.cheapestWin(): Long? {
    val det = ax * by - ay * bx
    // Collinear buttons don't occur in the input (checked); treat as unwinnable.
    if (det == 0L) return null
    val aNum = px * by - py * bx
    val bNum = ax * py - ay * px
    if (aNum % det != 0L || bNum % det != 0L) return null
    val a = aNum / det
    val b = bNum / det
    if (a !in 0..100 || b !in 0..100) return null
    return 3 * a + b
}
