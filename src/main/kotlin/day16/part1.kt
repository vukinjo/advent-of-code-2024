package com.github.mvukic.day16

import com.github.mvukic.linesForDay
import java.util.PriorityQueue

// Directions in clockwise order: East, South, West, North.
private val DR = intArrayOf(0, 1, 0, -1)
private val DC = intArrayOf(1, 0, -1, 0)

fun main() {
    val lines = linesForDay(16)
    println(lowestScore(lines))
}

// Dijkstra over (row, col, direction) states.
private fun lowestScore(grid: List<String>): Long {
    val rows = grid.size
    val cols = grid[0].length
    val start = find(grid, 'S')
    val end = find(grid, 'E')

    fun key(r: Int, c: Int, d: Int) = (r * cols + c) * 4 + d

    val dist = LongArray(rows * cols * 4) { Long.MAX_VALUE }
    val queue = PriorityQueue<Pair<Long, Int>>(compareBy { it.first })
    dist[key(start.first, start.second, 0)] = 0
    queue.add(0L to key(start.first, start.second, 0))

    while (queue.isNotEmpty()) {
        val (cost, state) = queue.poll()
        if (cost > dist[state]) continue
        val d = state % 4
        val cell = state / 4
        val r = cell / cols
        val c = cell % cols
        if (r == end.first && c == end.second) return cost

        val moves = mutableListOf(
            key(r, c, (d + 1) % 4) to cost + 1000,
            key(r, c, (d + 3) % 4) to cost + 1000,
        )
        val nr = r + DR[d]
        val nc = c + DC[d]
        if (grid[nr][nc] != '#') moves += key(nr, nc, d) to cost + 1
        for ((next, nextCost) in moves) {
            if (nextCost < dist[next]) {
                dist[next] = nextCost
                queue.add(nextCost to next)
            }
        }
    }
    error("End is unreachable")
}

private fun find(grid: List<String>, ch: Char): Pair<Int, Int> {
    val r = grid.indexOfFirst { ch in it }
    return r to grid[r].indexOf(ch)
}
