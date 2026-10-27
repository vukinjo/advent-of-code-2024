package com.github.mvukic.day24

import com.github.mvukic.linesForDay

fun main() {
    val (inputs, gates) = parse(linesForDay(24))
    println(simulate(inputs, gates) ?: error("Circuit does not settle"))
}
