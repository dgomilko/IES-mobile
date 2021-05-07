package com.example.modelperceptron

import android.os.SystemClock

class Perceptron(
        private val points: List<List<Int>>,
        private val threshold: Int,
        private val border: Int
) {
    private val weights = mutableListOf(0.0, 0.0)
    private var learningRate = 0.01
    private var timeDeadline = 0.0
    private var iterationsDeadline = 0

    private fun activation(point: List<Int>): Double =
            weights.mapIndexed { i, w -> w * point[i] }.sum()

    private fun checkFitness(): Boolean {
        val size = points.size
        return points
                    .slice(0 until border)
                    .all { point -> activation(point) > threshold } &&
                points
                    .slice(border until size)
                    .all { point -> activation(point) < threshold }
    }

    private fun updateWeights(point: List<Int>, activationRes: Double) {
        val delta = threshold - activationRes
        weights.mapIndexed { i, w -> weights[i] = w + delta * point[i] * learningRate }
    }

    fun setDeadline(iterations: Int) {
        iterationsDeadline = iterations
        timeDeadline = 0.0
    }
    fun setDeadline(time: Double) {
        timeDeadline = time * 1000
        iterationsDeadline = 0
    }
    fun setLearningRate(rate: Double) { learningRate = rate }

    private fun checkDeadline(count: Long): Boolean =
            if (timeDeadline != 0.0) count < timeDeadline else count < iterationsDeadline

    fun train(): Pair<List<Double>, Boolean> {
        weights.mapIndexed { i, _ -> weights[i] = 0.0 }
        var curCount = 0L
        val startTime = SystemClock.elapsedRealtime()
        while(checkDeadline(curCount)) {
            points.map { point ->
                if (checkFitness()) return Pair(weights, true)
                val y = activation(point)
                updateWeights(point, y)
                if (timeDeadline != 0.0) curCount = SystemClock.elapsedRealtime() - startTime
                else curCount++
            }
        }
        return Pair(weights, false)
    }
}
