package com.github.mvukic.day9

import com.github.mvukic.linesForDay

fun main() {
    val diskMap = linesForDay(9).joinToString("").trim()

    // Expand into blocks: file ID for file blocks, -1 for free space
    val blocks = mutableListOf<Int>()
    diskMap.forEachIndexed { index, char ->
        val value = if (index % 2 == 0) index / 2 else -1
        repeat(char.digitToInt()) { blocks.add(value) }
    }

    // Move file blocks from the end into the leftmost free space
    var left = 0
    var right = blocks.size - 1
    while (true) {
        while (left < blocks.size && blocks[left] != -1) left++
        while (right >= 0 && blocks[right] == -1) right--
        if (left >= right) break
        blocks[left] = blocks[right]
        blocks[right] = -1
    }

    val checksum = blocks.withIndex().filter { it.value != -1 }.sumOf { it.index.toLong() * it.value }
    println(checksum)
}
