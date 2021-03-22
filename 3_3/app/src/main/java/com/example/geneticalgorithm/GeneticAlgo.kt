package com.example.geneticalgorithm

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

class GeneticAlgo(private val coeffs: List<Double>, private val finalResult: Double) {
    private val variables = coeffs.size
    private val populationSize = 10
    private var population = mutableListOf<Equation>()

    private fun substitute(eq: Equation): Double {
        return coeffs.mapIndexed() { i, coeff -> coeff * eq.coeffs[i] }.sum()
    }

    private fun initPopulation() {
        val randLimit = ceil(finalResult / 2).toInt()
        for (i in 0 until populationSize) {
            val generatedCoeffs = (1..variables).map { (1..randLimit).random().toDouble() }
            population.add(Equation(generatedCoeffs))
        }
    }

    private fun fitness(): Equation? {
        population.map { equation ->
            val result = substitute(equation)
            val delta = abs(finalResult - result)
            if (delta == 0.0) return equation
            equation.delta = delta
        }

        val sumReversedDeltas = population.map{ e -> 1 / e.delta }.sum()
        population.map { e -> e.probability = 1 / e.delta / sumReversedDeltas }
        return null
    }

    private fun crossbreeding() {
        val newGeneration = mutableListOf<Equation>()
        val crossover = floor(variables.toDouble() / 2).toInt()
        for (i in 0 until populationSize) {
            val curPopulation = population.toMutableList()
            val parent1 = WeightedRandom(curPopulation).getRand()
            curPopulation.remove(parent1)
            val parent2 = WeightedRandom(curPopulation).getRand()
            val parent1Part = parent1.coeffs.slice(0 until crossover)
            val parent2Part = parent2.coeffs.slice(crossover until variables)
            val coeffs = parent1Part.plus(parent2Part).toMutableList()

            if ((1..100).random() < 10) {
                val chromosome = (0 until variables).random()
                val diff = listOf(-1, 1).random()
                coeffs[chromosome] = coeffs[chromosome] + diff
            }
            newGeneration.add(Equation(coeffs))
        }
        population = newGeneration
    }

    fun runAlgorithm(): List<Double> {
        initPopulation()

        while(true) {
            val result = fitness()
            if (result != null) return result.coeffs
            crossbreeding()
        }
    }
}