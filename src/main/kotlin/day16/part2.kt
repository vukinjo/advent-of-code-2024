package com.github.mvukic.day16

import com.github.mvukic.linesForDay
import java.util.PriorityQueue

// Directions in clockwise order: East, South, West, North.
private val DR = intArrayOf(0, 1, 0, -1)
private val DC = intArrayOf(1, 0, -1, 0)

fun main() {
    val lines = linesForDay(16)
    println(tilesOnBestPaths(lines))
}

// Dijkstra over (row, col, direction) states, then walk back from the end through
// every predecessor that lies on an optimal path and collect the tiles visited.
private fun tilesOnBestPaths(grid: List<String>): Int {
    val cols = grid[0].length
    val start = findTile(grid, 'S')
    val end = findTile(grid, 'E')

    fun key(r: Int, c: Int, d: Int) = (r * cols + c) * 4 + d

    val dist = LongArray(grid.size * cols * 4) { Long.MAX_VALUE }
    val queue = PriorityQueue<Pair<Long, Int>>(compareBy { it.first })
    dist[key(start.first, start.second, 0)] = 0
    queue.add(0L to key(start.first, start.second, 0))

    while (queue.isNotEmpty()) {
        val (cost, state) = queue.poll()
        if (cost > dist[state]) continue
        val d = state % 4
        val r = state / 4 / cols
        val c = state / 4 % cols
        val moves = mutableListOf(
            key(r, c, (d + 1) % 4) to cost + 1000,
            key(r, c, (d + 3) % 4) to cost + 1000,
        )
        if (grid[r + DR[d]][c + DC[d]] != '#') moves += key(r + DR[d], c + DC[d], d) to cost + 1
        for ((next, nextCost) in moves) {
            if (nextCost < dist[next]) {
                dist[next] = nextCost
                queue.add(nextCost to next)
            }
        }
    }

    val endStates = (0 until 4).map { key(end.first, end.second, it) }
    val best = endStates.minOf { dist[it] }
    val onBestPath = BooleanArray(dist.size)
    val stack = ArrayDeque(endStates.filter { dist[it] == best })
    stack.forEach { onBestPath[it] = true }
    while (stack.isNotEmpty()) {
        val state = stack.removeLast()
        val d = state % 4
        val r = state / 4 / cols
        val c = state / 4 % cols
        val predecessors = mutableListOf(
            key(r, c, (d + 1) % 4) to 1000L,
            key(r, c, (d + 3) % 4) to 1000L,
        )
        if (grid[r - DR[d]][c - DC[d]] != '#') predecessors += key(r - DR[d], c - DC[d], d) to 1L
        for ((prev, stepCost) in predecessors) {
            if (!onBestPath[prev] && dist[prev] != Long.MAX_VALUE && dist[prev] + stepCost == dist[state]) {
                onBestPath[prev] = true
                stack.addLast(prev)
            }
        }
    }
    return onBestPath.indices.filter { onBestPath[it] }.map { it / 4 }.toSet().size
}

private fun findTile(grid: List<String>, ch: Char): Pair<Int, Int> {
    val r = grid.indexOfFirst { ch in it }
    return r to grid[r].indexOf(ch)
}
