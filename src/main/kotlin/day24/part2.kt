package com.github.mvukic.day24

import com.github.mvukic.linesForDay
import kotlin.random.Random

fun main() {
    val (inputs, gates) = parse(linesForDay(24))
    val bits = inputs.keys.count { it.startsWith('x') }

    val suspicious = suspiciousWires(gates, bits)
    println("Suspicious wires: $suspicious")
    check(suspicious.size == 8) { "Expected 8 suspicious wires, found ${suspicious.size}" }

    // The structural rules only name the wires; confirm them by finding a pairing that makes the circuit add.
    val testCases = additionTestCases(inputs, bits)
    val pairing = pairings(suspicious.toList()).singleOrNull { pairs ->
        val fixed = swapOutputs(gates, pairs)
        testCases.all { (x, y) -> simulate(wireValues(x, y, bits), fixed) == x + y }
    } ?: error("No unique pairing of the suspicious wires turns the circuit into an adder")
    println("Swaps: $pairing (verified on ${testCases.size} additions)")

    println(suspicious.sorted().joinToString(","))
}

/**
 * Wires breaking the structure of a ripple-carry adder, where for bit i (i > 0):
 * `x XOR y -> s`, `x AND y -> c1`, `s XOR carryIn -> z`, `s AND carryIn -> c2`, `c1 OR c2 -> carryOut`,
 * bit 0 is a half adder and the last z-wire is the final carry.
 */
private fun suspiciousWires(gates: List<Gate>, bits: Int): Set<String> {
    val lastZ = "z%02d".format(bits)
    val consumers = gates.flatMap { g -> listOf(g.a to g, g.b to g) }.groupBy({ it.first }, { it.second })
    fun isInput(w: String) = w.startsWith('x') || w.startsWith('y')
    fun isFirstBit(g: Gate) = setOf(g.a, g.b) == setOf("x00", "y00")
    fun feedsInto(wire: String, op: String) = consumers[wire].orEmpty().any { it.op == op }

    return gates.filter { g ->
        when {
            // Every z-wire except the final carry is the output of a sum XOR.
            g.out.startsWith('z') && g.out != lastZ && g.op != "XOR" -> true
            g.out == lastZ && g.op != "OR" -> true
            // A XOR not taking x/y inputs is the final sum stage, so it must drive a z-wire.
            g.op == "XOR" && !isInput(g.a) && !g.out.startsWith('z') -> true
            // A x/y XOR (bit > 0) produces the half sum, which must feed the sum-stage XOR.
            g.op == "XOR" && isInput(g.a) && !isFirstBit(g) && !feedsInto(g.out, "XOR") -> true
            // Every AND (bit > 0) produces a partial carry, which must only feed the carry OR.
            g.op == "AND" && !isFirstBit(g) && consumers[g.out].orEmpty().any { it.op != "OR" } -> true
            else -> false
        }
    }.map { it.out }.toSet()
}

private fun swapOutputs(gates: List<Gate>, pairs: List<Pair<String, String>>): List<Gate> {
    val swap = pairs.flatMap { (a, b) -> listOf(a to b, b to a) }.toMap()
    return gates.map { g -> swap[g.out]?.let { g.copy(out = it) } ?: g }
}

/** All ways to split [wires] into unordered pairs. */
private fun pairings(wires: List<String>): List<List<Pair<String, String>>> {
    if (wires.isEmpty()) return listOf(emptyList())
    val first = wires.first()
    return wires.drop(1).flatMap { partner ->
        pairings(wires.drop(1) - partner).map { rest -> listOf(first to partner) + rest }
    }
}

/** The puzzle's own x/y values, edge cases, and a batch of random numbers. */
private fun additionTestCases(inputs: Map<String, Int>, bits: Int): List<Pair<Long, Long>> {
    fun number(prefix: Char) = inputs.filterKeys { it.startsWith(prefix) }.toSortedMap()
        .values.reversed().fold(0L) { acc, bit -> (acc shl 1) or bit.toLong() }
    val max = (1L shl bits) - 1
    val random = Random(2024)
    return listOf(number('x') to number('y'), 0L to 0L, max to max, max to 1L, 1L to max) +
        List(100) { random.nextLong(max + 1) to random.nextLong(max + 1) }
}

private fun wireValues(x: Long, y: Long, bits: Int): Map<String, Int> = buildMap {
    for (i in 0 until bits) {
        put("x%02d".format(i), ((x shr i) and 1).toInt())
        put("y%02d".format(i), ((y shr i) and 1).toInt())
    }
}
