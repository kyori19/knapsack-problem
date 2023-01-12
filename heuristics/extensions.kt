package heuristics

import knapsack.Item
import knapsack.KnapsackProblem
import utils.ensureSuccess
import kotlin.random.Random

fun KnapsackProblem.buildSolution(filter: (Item) -> Boolean) = Solution().apply {
    selection.addAll(items.filter(filter)).ensureSuccess()
}

fun KnapsackProblem.randomSolution(random: Random = Random) = buildSolution { random.nextBoolean() }
