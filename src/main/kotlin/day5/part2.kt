package com.github.mvukic.day5

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(5)

    // Rules as (before, after) pairs
    val rules = lines.filter { it.contains('|') }
        .map { it.split('|').let { (before, after) -> before.trim().toInt() to after.trim().toInt() } }
        .toSet()
    val updates = lines.filter { it.contains(',') }
        .map { line -> line.split(',').map { it.trim().toInt() } }

    // Rules are assumed to cover every pair of pages within an update, so they work as a comparator
    val comparator = Comparator<Int> { a, b ->
        when {
            a to b in rules -> -1
            b to a in rules -> 1
            else -> 0
        }
    }

    val sum = updates.filter { !isInOrder(it, rules) }
        .map { it.sortedWith(comparator) }
        .sumOf { it[it.size / 2].toLong() }
    println(sum)
}

// An update is in order if no later page is required to come before an earlier one
private fun isInOrder(update: List<Int>, rules: Set<Pair<Int, Int>>): Boolean {
    for (i in update.indices) {
        for (j in i + 1 until update.size) {
            if (update[j] to update[i] in rules) return false
        }
    }
    return true
}
