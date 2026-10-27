package com.github.mvukic.day20

import com.github.mvukic.linesForDay
import kotlin.math.abs

private const val MAX_CHEAT = 20

fun main() {
    val grid = linesForDay(20).filter { it.isNotBlank() }
    val savings = cheatSavings(grid)
    // The example track is tiny, so show its breakdown of savings >= 50; the real question asks for >= 100.
    if (grid.size < 20) {
        savings.filter { it >= 50 }.groupingBy { it }.eachCount().toSortedMap()
            .forEach { (saved, count) -> println("$count cheats save $saved") }
    }
    println(savings.count { it >= 100 })
}

// The track is a single path: order its cells by distance from the start; every pair of
// track cells within Manhattan distance MAX_CHEAT is a cheat saving (index gap - distance).
private fun cheatSavings(grid: List<String>): List<Int> {
    val path = trackInOrder(grid)
    val savings = mutableListOf<Int>()
    for (i in path.indices) {
        for (j in i + 1 until path.size) {
            val distance = abs(path[i].first - path[j].first) + abs(path[i].second - path[j].second)
            if (distance <= MAX_CHEAT) {
                val saved = (j - i) - distance
                if (saved > 0) savings += saved
            }
        }
    }
    return savings
}

private fun trackInOrder(grid: List<String>): List<Pair<Int, Int>> {
    val startRow = grid.indexOfFirst { 'S' in it }
    var current = startRow to grid[startRow].indexOf('S')
    var previous: Pair<Int, Int>? = null
    val path = mutableListOf(current)
    while (grid[current.first][current.second] != 'E') {
        val next = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)
            .map { (dr, dc) -> current.first + dr to current.second + dc }
            .first { it != previous && grid[it.first][it.second] != '#' }
        previous = current
        current = next
        path += current
    }
    return path
}
