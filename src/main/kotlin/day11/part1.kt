package com.github.mvukic.day11

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(11)
    val stones = lines.first().trim().split(" ").map { it.toLong() }

    // Order doesn't affect the count, so track how many stones carry each number.
    var counts = stones.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    repeat(25) {
        val next = mutableMapOf<Long, Long>()
        for ((stone, count) in counts) {
            for (newStone in blink(stone)) next.merge(newStone, count, Long::plus)
        }
        counts = next
    }
    println(counts.values.sum())
}

private fun blink(stone: Long): List<Long> {
    if (stone == 0L) return listOf(1L)
    val digits = stone.toString()
    if (digits.length % 2 == 0) {
        val half = digits.length / 2
        return listOf(digits.take(half).toLong(), digits.drop(half).toLong())
    }
    return listOf(stone * 2024)
}
