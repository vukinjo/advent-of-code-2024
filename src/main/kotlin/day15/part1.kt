package com.github.mvukic.day15

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(15)
    val separator = lines.indexOfFirst { it.isBlank() }
    val grid = lines.take(separator).map { it.toCharArray() }
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
        // Walk past any run of boxes; if it ends in free space, shift the run by one.
        var ex = x + dx
        var ey = y + dy
        while (grid[ey][ex] == 'O') {
            ex += dx
            ey += dy
        }
        if (grid[ey][ex] == '#') continue
        grid[ey][ex] = 'O' // first box jumps to the end of the run (overwritten below if nothing was pushed)
        grid[y + dy][x + dx] = '@'
        grid[y][x] = '.'
        x += dx
        y += dy
    }

    var sum = 0L
    for (row in grid.indices) for (col in grid[row].indices) {
        if (grid[row][col] == 'O') sum += 100L * row + col
    }
    println(sum)
}
