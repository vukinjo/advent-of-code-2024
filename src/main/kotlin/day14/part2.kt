package com.github.mvukic.day14

import com.github.mvukic.linesForDay

private const val WIDTH = 101
private const val HEIGHT = 103

fun main() {
    val lines = linesForDay(14)
    val robots = lines.filter { it.isNotBlank() }.map(::parseBot)

    // Positions repeat every WIDTH * HEIGHT seconds. The tree appears at the first moment
    // when no two robots share a tile.
    val seconds = (0 until WIDTH * HEIGHT).first { t ->
        robots.map { positionAt(it, t) }.toSet().size == robots.size
    }
    printGrid(robots.map { positionAt(it, seconds) }.toSet())
    println(seconds)
}

private data class Bot(val px: Int, val py: Int, val vx: Int, val vy: Int)

private fun parseBot(line: String): Bot {
    val (px, py, vx, vy) = Regex("""-?\d+""").findAll(line).map { it.value.toInt() }.toList()
    return Bot(px, py, vx, vy)
}

private fun positionAt(bot: Bot, t: Int) =
    (bot.px + bot.vx * t).mod(WIDTH) to (bot.py + bot.vy * t).mod(HEIGHT)

private fun printGrid(positions: Set<Pair<Int, Int>>) {
    for (y in 0 until HEIGHT) {
        println((0 until WIDTH).joinToString("") { x -> if (x to y in positions) "#" else "." })
    }
}
