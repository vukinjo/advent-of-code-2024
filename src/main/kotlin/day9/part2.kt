package com.github.mvukic.day9

import com.github.mvukic.linesForDay

private class Span(var start: Int, var length: Int)

fun main() {
    val diskMap = linesForDay(9).joinToString("").trim()

    // Files indexed by ID and free spans in disk order
    val files = mutableListOf<Span>()
    val free = mutableListOf<Span>()
    var position = 0
    diskMap.forEachIndexed { index, char ->
        val length = char.digitToInt()
        if (index % 2 == 0) files.add(Span(position, length)) else if (length > 0) free.add(Span(position, length))
        position += length
    }

    // Move each whole file, highest ID first, into the leftmost free span to its left that fits
    for (id in files.indices.reversed()) {
        val file = files[id]
        val span = free.firstOrNull { it.start < file.start && it.length >= file.length } ?: continue
        file.start = span.start
        span.start += file.length
        span.length -= file.length
        // Space freed behind the file is never used: later files have lower IDs and only move left of themselves
    }

    var checksum = 0L
    files.forEachIndexed { id, file ->
        for (block in file.start until file.start + file.length) checksum += block.toLong() * id
    }
    println(checksum)
}
