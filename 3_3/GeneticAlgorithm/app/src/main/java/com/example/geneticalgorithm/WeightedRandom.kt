package com.example.geneticalgorithm

import java.util.*

class WeightedRandom(elements: List<Equation>) {
    private val map: NavigableMap<Double, Equation> = TreeMap()
    private val random = Random()
    private var total = 0.0

    init {
        elements.map{ e ->
            total += e.probability
            map[total] = e
        }
    }

    fun getRand(): Equation {
        val value = random.nextDouble() * total
        return map.higherEntry(value)!!.value
    }
}