package com.github.mvukic.day10

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(10)
    // Non-digit tiles (only '.' in the examples) are impassable.
    val grid = lines.filter { it.isNotBlank() }.map { row -> row.map { if (it.isDigit()) it - '0' else -1 } }
    val memo = mutableMapOf<Pair<Int, Int>, Long>()

    var total = 0L
    for (y in grid.indices) for (x in grid[y].indices) {
        if (grid[y][x] == 0) total += trailCount(grid, x, y, memo)
    }
    println(total)
}

private val directions = listOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)

/** Number of distinct uphill trails from (x, y) to any 9, memoized per position. */
private fun trailCount(grid: List<List<Int>>, x: Int, y: Int, memo: MutableMap<Pair<Int, Int>, Long>): Long {
    memo[x to y]?.let { return it }
    val height = grid[y][x]
    val count = if (height == 9) 1L else directions
        .map { (dx, dy) -> x + dx to y + dy }
        .filter { (nx, ny) -> ny in grid.indices && nx in grid[ny].indices && grid[ny][nx] == height + 1 }
        .sumOf { (nx, ny) -> trailCount(grid, nx, ny, memo) }
    memo[x to y] = count
    return count
}
