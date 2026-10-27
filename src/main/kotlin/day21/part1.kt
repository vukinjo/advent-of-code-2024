package com.github.mvukic.day21

import com.github.mvukic.linesForDay

fun main() {
    val codes = linesForDay(21).filter { it.isNotBlank() }
    val chain = KeypadChain(robotKeypads = 2)
    codes.forEach { println("$it: ${chain.shortestLength(it)}") }
    println(codes.sumOf(chain::complexity))
}
