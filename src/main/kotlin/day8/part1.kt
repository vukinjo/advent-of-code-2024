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
                // Antinode beyond b, at the same distance as a is from b
                val row = 2 * b.first - a.first
                val column = 2 * b.second - a.second
                if (row in 0 until height && column in 0 until width) antinodes.add(row to column)
            }
        }
    }

    println(antinodes.size)
}
