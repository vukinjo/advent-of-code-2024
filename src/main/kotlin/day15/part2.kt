package com.github.mvukic.day15

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(15)
    val separator = lines.indexOfFirst { it.isBlank() }
    val grid = lines.take(separator).map { widen(it).toCharArray() }
    val moves = lines.drop(separator + 1).joinToString("").trim()

    var y = grid.indexOfFirst { '@' in it }
    var x = grid[y].indexOf('@')
    for (move in moves) {
        val (dx, dy) = when (move) {
            '^' -> 0 to -1
            'v' -> 0 to 1
            '<' -> -1 to 0
            '>' -> 1 to 0
            else -> continue
        }
        val toMove = cellsToPush(grid, x, y, dx, dy) ?: continue
        // Move the farthest cells first so nothing is overwritten before it moves.
        for ((cx, cy) in toMove.sortedByDescending { (cx, cy) -> cx * dx + cy * dy }) {
            grid[cy + dy][cx + dx] = grid[cy][cx]
            grid[cy][cx] = '.'
        }
        x += dx
        y += dy
    }

    var sum = 0L
    for (row in grid.indices) for (col in grid[row].indices) {
        if (grid[row][col] == '[') sum += 100L * row + col
    }
    println(sum)
}

private fun widen(row: String) = buildString {
    for (c in row) {
        append(
            when (c) {
                '#' -> "##"
                'O' -> "[]"
                '@' -> "@."
                else -> ".."
            }
        )
    }
}

/**
 * Every cell (the robot and box halves) that moves when the robot at (x, y) steps by (dx, dy),
 * or null if anything would hit a wall. Touching either half of a box pulls in the whole box.
 */
private fun cellsToPush(grid: List<CharArray>, x: Int, y: Int, dx: Int, dy: Int): Set<Pair<Int, Int>>? {
    val result = linkedSetOf(x to y)
    val queue = ArrayDeque(listOf(x to y))
    while (queue.isNotEmpty()) {
        val (cx, cy) = queue.removeFirst()
        val nx = cx + dx
        val ny = cy + dy
        val next = when (grid[ny][nx]) {
            '#' -> return null
            '[' -> listOf(nx to ny, nx + 1 to ny)
            ']' -> listOf(nx to ny, nx - 1 to ny)
            else -> emptyList()
        }
        for (cell in next) if (result.add(cell)) queue.addLast(cell)
    }
    return result
}
