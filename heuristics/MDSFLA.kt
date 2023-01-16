package heuristics

import knapsack.KnapsackProblem
import reports.Report
import utils.ReplaceableCollection
import utils.toReplaceableDiscreteList
import kotlin.random.Random

data class MDSFLA(
    val groupCount: Int,
    val groupSize: Int,
    val localIter: Int,
    val globalIter: Int,
    val random: Random = Random,
) {

    private val KnapsackProblem.Solution.fitness
        get() = profit

    fun solve(problem: KnapsackProblem) = Report {
        problem.run {
            val frogs = MutableList(groupSize * groupCount) { randomSolution(random).apply { makeValidGreedy() } }

            repeat(globalIter) { i ->
                frogs.sortByDescending { it.fitness }

                frogs.toReplaceableDiscreteList(groupCount)
                    .forEach { group ->
                        repeat(localIter) { j ->
                            update(group, frogs.best.also { report(i to j, it.fitness) })
                        }
                    }
            }

            frogs.best.also { report(globalIter to 0, it.fitness) }
        }
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
        replace(worst, solution.apply { makeValidGreedy() })
    }

    private infix fun Int.to(other: Int) = Key(this, other)

    data class Key(
        val first: Int,
        val second: Int,
    ) {
        override fun toString() = "$first, $second"
    }
}
