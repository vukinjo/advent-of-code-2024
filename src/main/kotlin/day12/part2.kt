package com.github.mvukic.day12

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(12)
    val grid = lines.filter { it.isNotBlank() }
    val visited = Array(grid.size) { BooleanArray(grid[it].length) }

    var total = 0L
    for (y in grid.indices) for (x in grid[y].indices) {
        if (visited[y][x]) continue
        val region = collectRegion(grid, visited, x, y)
        total += region.size.toLong() * countSides(region)
    }
    println(total)
}

private val directions = listOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)

private fun collectRegion(grid: List<String>, visited: Array<BooleanArray>, x: Int, y: Int): Set<Pair<Int, Int>> {
    val plant = grid[y][x]
    val region = mutableSetOf(x to y)
    val stack = ArrayDeque(listOf(x to y))
    visited[y][x] = true
    while (stack.isNotEmpty()) {
        val (cx, cy) = stack.removeLast()
        for ((dx, dy) in directions) {
            val nx = cx + dx
            val ny = cy + dy
            if (ny in grid.indices && nx in grid[ny].indices && !visited[ny][nx] && grid[ny][nx] == plant) {
                visited[ny][nx] = true
                region += nx to ny
                stack.addLast(nx to ny)
            }
        }
    }
    return region
}

/**
 * A polygon has as many sides as corners. For each cell and each diagonal direction:
 * it's an outer corner if both orthogonal neighbours are outside the region, and an inner
 * corner if both are inside but the diagonal cell is outside.
 */
private fun countSides(region: Set<Pair<Int, Int>>): Int {
    var corners = 0
    for ((x, y) in region) {
        for ((dx, dy) in listOf(1 to 1, 1 to -1, -1 to 1, -1 to -1)) {
            val horizontal = (x + dx to y) in region
            val vertical = (x to y + dy) in region
            val diagonal = (x + dx to y + dy) in region
            if (!horizontal && !vertical) corners++
            if (horizontal && vertical && !diagonal) corners++
        }
    }
    return corners
}
