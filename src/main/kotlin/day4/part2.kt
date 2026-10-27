package com.github.mvukic.day4

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(4).filter { it.isNotBlank() }.map { it.toCharArray() }

    var count = 0
    // Every X-MAS has an 'A' in the middle, so check both diagonals around each inner 'A'
    for (row in 1 until lines.size - 1) {
        for (column in 1 until lines[row].size - 1) {
            if (lines[row][column] != 'A') continue
            val diagonal1 = "${lines[row - 1][column - 1]}${lines[row + 1][column + 1]}"
            val diagonal2 = "${lines[row - 1][column + 1]}${lines[row + 1][column - 1]}"
            if (isMasDiagonal(diagonal1) && isMasDiagonal(diagonal2)) count++
        }
    }

    println(count)
}

// Diagonal ends must be one 'M' and one 'S' (MAS forwards or backwards)
private fun isMasDiagonal(ends: String) = ends == "MS" || ends == "SM"
