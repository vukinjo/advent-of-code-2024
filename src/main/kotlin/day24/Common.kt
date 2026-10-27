package com.github.mvukic.day24

internal data class Gate(val a: String, val op: String, val b: String, val out: String)

internal fun parse(lines: List<String>): Pair<Map<String, Int>, List<Gate>> {
    val inputs = lines.filter { ':' in it }.associate { line ->
        val (wire, value) = line.split(":")
        wire.trim() to value.trim().toInt()
    }
    val gates = lines.filter { "->" in it }.map { line ->
        val (a, op, b, _, out) = line.trim().split(" ")
        Gate(a, op, b, out)
    }
    return inputs to gates
}

/**
 * Simulates the circuit and combines the z-wires into a number, z00 being the lowest bit.
 * Returns null if some z-wire never gets a value (possible when outputs are swapped into a loop).
 */
internal fun simulate(inputs: Map<String, Int>, gates: List<Gate>): Long? {
    val values = HashMap(inputs)
    var pending = gates
    while (pending.isNotEmpty()) {
        val (ready, waiting) = pending.partition { it.a in values && it.b in values }
        if (ready.isEmpty()) break
        for (gate in ready) {
            val a = values.getValue(gate.a)
            val b = values.getValue(gate.b)
            values[gate.out] = when (gate.op) {
                "AND" -> a and b
                "OR" -> a or b
                "XOR" -> a xor b
                else -> error("Unknown gate ${gate.op}")
            }
        }
        pending = waiting
    }
    val zWires = gates.map { it.out }.filter { it.startsWith('z') }.sortedDescending()
    if (zWires.any { it !in values }) return null
    return zWires.fold(0L) { acc, wire -> (acc shl 1) or values.getValue(wire).toLong() }
}
