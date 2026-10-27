package com.github.mvukic.day19

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(19)
    val towels = lines.first().split(",").map { it.trim() }
    val designs = lines.drop(1).filter { it.isNotBlank() }
    println(designs.count { isPossible(it, towels) })
}

// possible[i] is true when the first i stripes of the design can be built from towels.
private fun isPossible(design: String, towels: List<String>): Boolean {
    val possible = BooleanArray(design.length + 1)
    possible[0] = true
    for (i in 0 until design.length) {
        if (!possible[i]) continue
        for (towel in towels) {
            if (design.startsWith(towel, i)) possible[i + towel.length] = true
        }
    }
    return possible[design.length]
}
