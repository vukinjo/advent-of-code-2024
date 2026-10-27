package com.github.mvukic.day22

import com.github.mvukic.linesForDay

fun main() {
    val secrets = linesForDay(22).filter { it.isNotBlank() }.map { it.trim().toLong() }
    println(mostBananas(secrets, steps = 2000))
}

private const val PRUNE_MASK = 16777216L - 1 // modulo 2^24

private fun nextSecret(secret: Long): Long {
    var s = (secret xor (secret * 64)) and PRUNE_MASK
    s = (s xor (s / 32)) and PRUNE_MASK
    s = (s xor (s * 2048)) and PRUNE_MASK
    return s
}

/**
 * For every window of four consecutive price changes, adds up the price at its first occurrence for each buyer and
 * returns the best total. A window is encoded as a base-19 number (each change shifted from -9..9 to 0..18).
 */
private fun mostBananas(secrets: List<Long>, steps: Int): Int {
    val totals = IntArray(19 * 19 * 19 * 19)
    val lastSeenBy = IntArray(totals.size) { -1 }
    secrets.forEachIndexed { buyer, initial ->
        val prices = generateSequence(initial, ::nextSecret).take(steps + 1).map { (it % 10).toInt() }.toList()
        var key = 0
        for (i in 1..steps) {
            key = (key * 19 + (prices[i] - prices[i - 1] + 9)) % totals.size
            if (i >= 4 && lastSeenBy[key] != buyer) {
                lastSeenBy[key] = buyer
                totals[key] += prices[i]
            }
        }
    }
    return totals.max()
}
