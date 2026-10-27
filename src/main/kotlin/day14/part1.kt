package com.github.mvukic.day14

import com.github.mvukic.linesForDay

fun main() {
    val lines = linesForDay(14)
    val robots = lines.filter { it.isNotBlank() }.map(::parseRobot)

    // The example uses an 11x7 space; the real input uses 101x103. Small inputs mean the example.
    val (width, height) = if (robots.size <= 12) 11 to 7 else 101 to 103

    val quadrants = LongArray(4)
    for (robot in robots) {
        val x = (robot.px + robot.vx * 100).mod(width)
        val y = (robot.py + robot.vy * 100).mod(height)
        if (x == width / 2 || y == height / 2) continue
        val quadrant = (if (x > width / 2) 1 else 0) + (if (y > height / 2) 2 else 0)
        quadrants[quadrant]++
    }
    println(quadrants.reduce(Long::times))
}

private data class Robot(val px: Int, val py: Int, val vx: Int, val vy: Int)

private val numberRegex = Regex("""-?\d+""")

private fun parseRobot(line: String): Robot {
    val (px, py, vx, vy) = numberRegex.findAll(line).map { it.value.toInt() }.toList()
    return Robot(px, py, vx, vy)
}
