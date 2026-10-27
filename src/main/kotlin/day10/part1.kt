package com.github.mvukic.day10

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(10)
    // Non-digit tiles (only '.' in the examples) are impassable.
    val grid = lines.filter { it.isNotBlank() }.map { row -> row.map { if (it.isDigit()) it - '0' else -1 } }

    var total = 0
    for (y in grid.indices) for (x in grid[y].indices) {
        if (grid[y][x] == 0) total += reachableNines(grid, x, y).size
    }
    println(total)
}

private val directions = listOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)

private fun reachableNines(grid: List<List<Int>>, x: Int, y: Int): Set<Pair<Int, Int>> {
    val height = grid[y][x]
    if (height == 9) return setOf(x to y)
    return directions
        .map { (dx, dy) -> x + dx to y + dy }
        .filter { (nx, ny) -> ny in grid.indices && nx in grid[ny].indices && grid[ny][nx] == height + 1 }
        .flatMapTo(mutableSetOf()) { (nx, ny) -> reachableNines(grid, nx, ny) }
}
