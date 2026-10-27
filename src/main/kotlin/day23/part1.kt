package com.github.mvukic.day23

import com.github.mvukic.linesForDay

fun main() {
    val graph = parseGraph(linesForDay(23))
    val triangles = triangles(graph)
    println(triangles.count { triangle -> triangle.any { it.startsWith('t') } })
}

private fun parseGraph(lines: List<String>): Map<String, Set<String>> {
    val graph = HashMap<String, MutableSet<String>>()
    lines.filter { it.isNotBlank() }.forEach { line ->
        val (a, b) = line.trim().split("-")
        graph.getOrPut(a) { HashSet() }.add(b)
        graph.getOrPut(b) { HashSet() }.add(a)
    }
    return graph
}

/** All sets of three mutually connected computers, each listed once in sorted order (a < b < c). */
private fun triangles(graph: Map<String, Set<String>>): List<List<String>> = buildList {
    for ((a, neighbours) in graph) {
        for (b in neighbours) {
            if (b <= a) continue
            for (c in neighbours) {
                if (c > b && c in graph.getValue(b)) add(listOf(a, b, c))
            }
        }
    }
}
