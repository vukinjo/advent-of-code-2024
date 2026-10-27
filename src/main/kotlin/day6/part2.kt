package com.github.mvukic.day6

import com.github.mvukic.linesForDay

// Directions in turn-right order: up, right, down, left
private val rowDeltas = intArrayOf(-1, 0, 1, 0)
private val columnDeltas = intArrayOf(0, 1, 0, -1)

fun main() {
    val grid = linesForDay(6).filter { it.isNotBlank() }.map { it.toCharArray() }
    val height = grid.size
    val width = grid[0].size

    var startRow = 0
    var startColumn = 0
    var startDirection = 0
    for (row in grid.indices) {
        for (column in grid[row].indices) {
            val direction = "^>v<".indexOf(grid[row][column])
            if (direction >= 0) {
                startRow = row
                startColumn = column
                startDirection = direction
            }
        }
    }

    // Only cells on the original route can change the guard's path
    val route = mutableSetOf<Pair<Int, Int>>()
    patrol(grid, startRow, startColumn, startDirection) { row, column -> route.add(row to column) }
    route.remove(startRow to startColumn)

    var count = 0
    for ((row, column) in route) {
        grid[row][column] = '#'
        if (patrol(grid, startRow, startColumn, startDirection)) count++
        grid[row][column] = '.'
    }

    println(count)
}

// Walks the guard until it leaves the grid (returns false) or repeats a position+direction (returns true)
private fun patrol(
    grid: List<CharArray>,
    startRow: Int,
    startColumn: Int,
    startDirection: Int,
    onVisit: (Int, Int) -> Unit = { _, _ -> },
): Boolean {
    val height = grid.size
    val width = grid[0].size
    val seen = BooleanArray(height * width * 4)
    var row = startRow
    var column = startColumn
    var direction = startDirection
    while (true) {
        val state = (row * width + column) * 4 + direction
        if (seen[state]) return true
        seen[state] = true
        onVisit(row, column)

        val nextRow = row + rowDeltas[direction]
        val nextColumn = column + columnDeltas[direction]
        if (nextRow !in 0 until height || nextColumn !in 0 until width) return false
        if (grid[nextRow][nextColumn] == '#') {
            direction = (direction + 1) % 4
        } else {
            row = nextRow
            column = nextColumn
        }
    }
}
