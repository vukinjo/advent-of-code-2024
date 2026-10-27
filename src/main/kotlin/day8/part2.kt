package com.github.mvukic.day8

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(8).filter { it.isNotBlank() }
    val height = lines.size
    val width = lines[0].length

    // Antenna positions grouped by frequency
    val antennas = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    for (row in lines.indices) {
        for (column in lines[row].indices) {
            val char = lines[row][column]
            if (char != '.') antennas.getOrPut(char) { mutableListOf() }.add(row to column)
        }
    }

    val antinodes = mutableSetOf<Pair<Int, Int>>()
    for (positions in antennas.values) {
        for (a in positions) {
            for (b in positions) {
                if (a == b) continue
                // Every grid position from b onwards in the a -> b direction, in steps of the pair's distance
                // (the reverse direction is covered by the pair (b, a))
                val rowStep = b.first - a.first
                val columnStep = b.second - a.second
                var row = b.first
                var column = b.second
                while (row in 0 until height && column in 0 until width) {
                    antinodes.add(row to column)
                    row += rowStep
                    column += columnStep
                }
            }
        }
    }

    println(antinodes.size)
}
