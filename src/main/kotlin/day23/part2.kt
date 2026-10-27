package com.github.mvukic.day23

import com.github.mvukic.linesForDay

fun main() {
    val graph = parseGraph(linesForDay(23))
    println(largestClique(graph).sorted().joinToString(","))
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

/** Maximum clique via Bron–Kerbosch with pivoting. */
private fun largestClique(graph: Map<String, Set<String>>): Set<String> {
    var best = emptySet<String>()

    fun expand(clique: Set<String>, candidates: MutableSet<String>, excluded: MutableSet<String>) {
        if (candidates.isEmpty() && excluded.isEmpty()) {
            if (clique.size > best.size) best = clique
            return
        }
        if (clique.size + candidates.size <= best.size) return
        val pivot = (candidates + excluded).maxBy { (graph.getValue(it) intersect candidates).size }
        for (v in candidates - graph.getValue(pivot)) {
            val neighbours = graph.getValue(v)
            expand(clique + v, candidates.intersect(neighbours).toMutableSet(), excluded.intersect(neighbours).toMutableSet())
            candidates -= v
            excluded += v
        }
    }

    expand(emptySet(), graph.keys.toMutableSet(), mutableSetOf())
    return best
}
