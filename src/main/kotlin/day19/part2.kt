package com.github.mvukic.day19

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(19)
    val towels = lines.first().split(",").map { it.trim() }
    val designs = lines.drop(1).filter { it.isNotBlank() }
    println(designs.sumOf { countArrangements(it, towels) })
}

// ways[i] is the number of ways to build the first i stripes of the design.
private fun countArrangements(design: String, towels: List<String>): Long {
    val ways = LongArray(design.length + 1)
    ways[0] = 1
    for (i in 0 until design.length) {
        if (ways[i] == 0L) continue
        for (towel in towels) {
            if (design.startsWith(towel, i)) ways[i + towel.length] += ways[i]
        }
    }
    return ways[design.length]
}
