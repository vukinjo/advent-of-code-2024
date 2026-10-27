package com.github.mvukic.day22

import com.github.mvukic.linesForDay

fun main() {
    val secrets = linesForDay(22).filter { it.isNotBlank() }.map { it.trim().toLong() }
    println(secrets.sumOf { secret -> generateSequence(secret, ::nextSecret).elementAt(2000) })
}

private const val PRUNE_MASK = 16777216L - 1 // modulo 2^24

private fun nextSecret(secret: Long): Long {
    var s = (secret xor (secret * 64)) and PRUNE_MASK
    s = (s xor (s / 32)) and PRUNE_MASK
    s = (s xor (s * 2048)) and PRUNE_MASK
    return s
}
