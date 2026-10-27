package com.github.mvukic.day20

import com.github.mvukic.linesForDay
import kotlin.math.abs

fun main() {
    val grid = linesForDay(20).filter { it.isNotBlank() }
    val savings = cheatSavings(grid)
    // The example track is tiny, so show its full breakdown; the real question asks for savings >= 100.
    if (grid.size < 20) {
        savings.groupingBy { it }.eachCount().toSortedMap().forEach { (saved, count) -> println("$count cheats save $saved") }
    }
    println(savings.count { it >= 100 })
}

// The track is a single path: order its cells by distance from the start,
// then every pair of track cells within Manhattan distance 2 is a cheat.
private fun cheatSavings(grid: List<String>): List<Int> {
    val path = trackInOrder(grid)
    val savings = mutableListOf<Int>()
    for (i in path.indices) {
        for (j in i + 1 until path.size) {
            val distance = abs(path[i].first - path[j].first) + abs(path[i].second - path[j].second)
            if (distance <= 2) {
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
