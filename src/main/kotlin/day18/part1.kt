package com.github.mvukic.day18

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(18)
    val bytes = lines.filter { it.isNotBlank() }.map { line -> line.split(",").let { it[0].toInt() to it[1].toInt() } }
    // The example uses a 7x7 grid with 12 bytes; the real input a 71x71 grid with 1024 bytes.
    val (size, fallen) = if (bytes.size < 100) 7 to 12 else 71 to 1024
    println(shortestPath(size, bytes.take(fallen).toSet()))
}

// BFS from the top-left to the bottom-right corner; -1 if unreachable.
private fun shortestPath(size: Int, corrupted: Set<Pair<Int, Int>>): Int {
    val dist = Array(size) { IntArray(size) { -1 } }
    val queue = ArrayDeque<Pair<Int, Int>>()
    dist[0][0] = 0
    queue.add(0 to 0)
    while (queue.isNotEmpty()) {
        val (x, y) = queue.removeFirst()
        if (x == size - 1 && y == size - 1) return dist[y][x]
        for ((dx, dy) in listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)) {
            val nx = x + dx
            val ny = y + dy
            if (nx !in 0 until size || ny !in 0 until size) continue
            if (dist[ny][nx] != -1 || (nx to ny) in corrupted) continue
            dist[ny][nx] = dist[y][x] + 1
            queue.add(nx to ny)
        }
    }
    return -1
}
