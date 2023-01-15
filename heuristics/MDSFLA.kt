package heuristics

import knapsack.KnapsackProblem
import utils.ReplaceableCollection
import utils.squared
import utils.toReplaceableDiscreteList
import kotlin.random.Random

data class MDSFLA(
    val groupCount: Int,
    val groupSize: Int,
    val localIter: Int,
    val globalIter: Int,
    val random: Random = Random,
) {

    private val KnapsackProblem.maxPerformance: Int
        get() = items.maxOf { it.performance }

    private val KnapsackProblem.Solution.fitness: Int
        get() = profit.takeIf { valid } ?: (profit - (problem.maxPerformance * (weight - problem.capacity)).squared())

    fun solve(problem: KnapsackProblem): KnapsackProblem.Solution = problem.run {
        val frogs = MutableList(groupSize * groupCount) { randomSolution(random) }

        repeat(globalIter) { _ ->
            frogs.sortByDescending { it.fitness }

            frogs.toReplaceableDiscreteList(groupCount)
                .forEach { group ->
                    repeat(localIter) { update(group, frogs.best) }
                }
        }

        frogs.best
    }

    private val Collection<KnapsackProblem.Solution>.best: KnapsackProblem.Solution
        get() = maxBy { it.fitness }

    private val Collection<KnapsackProblem.Solution>.worst: KnapsackProblem.Solution
        get() = minBy { it.fitness }

    private fun KnapsackProblem.update(
        group: ReplaceableCollection<KnapsackProblem.Solution>,
        globalBest: KnapsackProblem.Solution
    ) = group.run {
        val solution = buildSolution { item ->
            listOf(item in best, item in worst, random.nextBoolean()).count { it } >= 2
        }.takeIf { it.fitness > worst.fitness } ?: buildSolution { item ->
            listOf(item in globalBest, item in worst, random.nextBoolean()).count { it } >= 2
        }.takeIf { it.fitness > worst.fitness } ?: randomSolution(random)
        replace(worst, solution)
    }
}
