package com.github.mvukic.day18

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(18)
    val bytes = lines.filter { it.isNotBlank() }.map { line -> line.split(",").let { it[0].toInt() to it[1].toInt() } }
    // The example uses a 7x7 grid; the real input a 71x71 grid.
    val size = if (bytes.size < 100) 7 else 71

    // Binary search for the smallest number of fallen bytes that blocks the exit.
    var low = 0
    var high = bytes.size
    while (low < high) {
        val mid = (low + high) / 2
        if (isReachable(size, bytes.take(mid).toSet())) low = mid + 1 else high = mid
    }
    check(low in 1..bytes.size && !isReachable(size, bytes.take(low).toSet())) { "The exit never gets cut off" }
    val (x, y) = bytes[low - 1]
    println("$x,$y")
}

private fun isReachable(size: Int, corrupted: Set<Pair<Int, Int>>): Boolean {
    val seen = Array(size) { BooleanArray(size) }
    val queue = ArrayDeque<Pair<Int, Int>>()
    seen[0][0] = true
    queue.add(0 to 0)
    while (queue.isNotEmpty()) {
        val (x, y) = queue.removeFirst()
        if (x == size - 1 && y == size - 1) return true
        for ((dx, dy) in listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)) {
            val nx = x + dx
            val ny = y + dy
            if (nx !in 0 until size || ny !in 0 until size) continue
            if (seen[ny][nx] || (nx to ny) in corrupted) continue
            seen[ny][nx] = true
            queue.add(nx to ny)
        }
    }
    return false
}
