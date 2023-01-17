package heuristics

import knapsack.KnapsackProblem
import reports.Report
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.exp
import kotlin.random.Random

data class OWOA(
    val agentCount: Int,
    val iter: Int,
    val b: Double,
    val random: Random = Random,
) {

    private operator fun KnapsackProblem.Solution.not(): KnapsackProblem.Solution =
        problem.buildSolution { it !in this }

    private fun KnapsackProblem.Solution.vectorised(): List<Double> =
        problem.items.map {
            if (it in this) {
                1.0
            } else {
                -1.0
            }
        }

    private fun List<Double>.toSolution(problem: KnapsackProblem) = problem.run {
        Solution().also {
            forEachIndexed { i, d ->
                if (d >= 0) {
                    it.selection.add(items[i])
                }
            }
        }
    }

    private operator fun List<Double>.times(other: Double): List<Double> = this * List(size) { other }

    private operator fun List<Double>.times(other: List<Double>): List<Double> = zip(other) { d1, d2 -> d1 * d2 }

    private operator fun List<Double>.plus(other: List<Double>): List<Double> = zip(other) { d1, d2 -> d1 + d2 }

    private operator fun List<Double>.minus(other: List<Double>): List<Double> = zip(other) { d1, d2 -> d1 - d2 }

    private fun List<Double>.norm(): List<Double> = map { it.absoluteValue }

    private fun KnapsackProblem.Solution.move(best: KnapsackProblem.Solution, a: Double): KnapsackProblem.Solution = problem.run {
        when (random.nextBoolean()) {
            true -> {
                val d = ((randomSolution(random).vectorised() * 2.0 * best.vectorised()) - vectorised()).norm()
                best.vectorised() - randomSolution(random).vectorised() * a * d
            }
            false -> {
                val d = (best.vectorised() - vectorised()).norm()
                val l = random.nextDouble(-1.0, 1.0)
                best.vectorised() + d * exp(b * l) * cos(2 * Math.PI * l)
            }
        }
            .toSolution(this)
    }

    fun solve(problem: KnapsackProblem) = Report {
        problem.run {
            var whales = List(agentCount) { randomSolution(random).apply { makeValidGreedy() } }
            var best = whales.first()
            repeat(iter) { i ->
                best = (listOf(best) + whales + whales.map { !it }.filter { it.valid }).maxBy { it.profit }
                report(i, best.profit)
                whales = whales.map { it.move(best, 2.0 * (iter - i) / iter).apply { makeValidGreedy() } }
            }
            (listOf(best) + whales).maxBy { it.profit }
                .also { report(iter, it.profit) }
        }
    }
}
