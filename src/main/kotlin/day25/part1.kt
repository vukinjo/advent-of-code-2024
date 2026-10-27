package com.github.mvukic.day25

import com.github.mvukic.linesForDay

fun main() {
    val schematics = parseSchematics(linesForDay(25))
    val (locks, keys) = schematics.partition { it.first().all { c -> c == '#' } }
    val lockHeights = locks.map(::heights)
    val keyHeights = keys.map(::heights)
    // Every schematic has the same height; a column fits if pin + key don't exceed the free rows in between.
    val space = schematics.first().size - 2
    println(lockHeights.sumOf { lock -> keyHeights.count { key -> lock.zip(key).all { (l, k) -> l + k <= space } } })
}

private fun parseSchematics(lines: List<String>): List<List<String>> {
    val result = mutableListOf<List<String>>()
    var current = mutableListOf<String>()
    for (line in lines + "") {
        if (line.isBlank()) {
            if (current.isNotEmpty()) result += current
            current = mutableListOf()
        } else {
            current += line.trim()
        }
    }
    return result
}

/** Column heights, not counting the full top (lock) or bottom (key) row. */
private fun heights(schematic: List<String>): List<Int> =
    schematic.first().indices.map { col -> schematic.count { it[col] == '#' } - 1 }
