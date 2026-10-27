package com.github.mvukic.day21

import kotlin.math.abs

private data class Pos(val row: Int, val col: Int)

private class Keypad(vararg rows: String) {
    private val keys: Map<Char, Pos> = buildMap {
        rows.forEachIndexed { r, line -> line.forEachIndexed { c, ch -> if (ch != ' ') put(ch, Pos(r, c)) } }
    }
    private val gap: Pos = rows.withIndex().firstNotNullOf { (r, line) ->
        line.indexOf(' ').takeIf { it >= 0 }?.let { Pos(r, it) }
    }

    /**
     * Candidate move sequences (each ending with 'A') from [from] to [to]: "all horizontal then all vertical" and
     * the reverse, minus any whose corner is the gap. Zig-zag paths are never cheaper, since they only add direction
     * changes for the controlling keypad. Callers pick the cheaper candidate by recursive cost, not by a fixed rule.
     */
    fun paths(from: Char, to: Char): List<String> {
        val a = keys.getValue(from)
        val b = keys.getValue(to)
        val horizontal = (if (b.col > a.col) ">" else "<").repeat(abs(b.col - a.col))
        val vertical = (if (b.row > a.row) "v" else "^").repeat(abs(b.row - a.row))
        return buildSet {
            if (Pos(a.row, b.col) != gap) add(horizontal + vertical + "A")
            if (Pos(b.row, a.col) != gap) add(vertical + horizontal + "A")
        }.toList()
    }
}

private val numericPad = Keypad("789", "456", "123", " 0A")
private val directionalPad = Keypad(" ^A", "<v>")

/** Memoized cost, in presses on the human's keypad, of typing on a directional keypad `depth` robots away. */
internal class KeypadChain(private val robotKeypads: Int) {
    private val memo = HashMap<Triple<Char, Char, Int>, Long>()

    /** Presses needed to move that keypad's arm from [from] to [to] and press it. Depth 0 is the human's keypad. */
    private fun move(from: Char, to: Char, depth: Int): Long {
        if (depth == 0) return 1
        return memo.getOrPut(Triple(from, to, depth)) {
            directionalPad.paths(from, to).minOf { sequence(it, depth - 1) }
        }
    }

    /** Presses needed to type [seq] on that keypad, starting with its arm on 'A'. */
    private fun sequence(seq: String, depth: Int): Long =
        "A$seq".zipWithNext().sumOf { (a, b) -> move(a, b, depth) }

    /** Fewest human presses to type [code] on the numeric keypad through [robotKeypads] robot-operated keypads. */
    fun shortestLength(code: String): Long =
        "A$code".zipWithNext().sumOf { (a, b) ->
            numericPad.paths(a, b).minOf { sequence(it, robotKeypads) }
        }

    fun complexity(code: String): Long = shortestLength(code) * code.filter(Char::isDigit).toLong()
}
