package com.github.mvukic.day12

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(12)
    val grid = lines.filter { it.isNotBlank() }
    val visited = Array(grid.size) { BooleanArray(grid[it].length) }

    var total = 0L
    for (y in grid.indices) for (x in grid[y].indices) {
        if (visited[y][x]) continue
        val (area, perimeter) = measureRegion(grid, visited, x, y)
        total += area.toLong() * perimeter
    }
    println(total)
}

private val directions = listOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)

/** Flood-fills the region containing (x, y), returning its area and perimeter. */
private fun measureRegion(grid: List<String>, visited: Array<BooleanArray>, x: Int, y: Int): Pair<Int, Int> {
    val plant = grid[y][x]
    val stack = ArrayDeque(listOf(x to y))
    visited[y][x] = true
    var area = 0
    var perimeter = 0
    while (stack.isNotEmpty()) {
        val (cx, cy) = stack.removeLast()
        area++
        for ((dx, dy) in directions) {
            val nx = cx + dx
            val ny = cy + dy
            if (ny !in grid.indices || nx !in grid[ny].indices || grid[ny][nx] != plant) {
                perimeter++
            } else if (!visited[ny][nx]) {
                visited[ny][nx] = true
                stack.addLast(nx to ny)
            }
        }
    }
    return area to perimeter
}
