package knapsack

data class KnapsackProblem(
        val capacity: Int,
        val optimal: List<Pair<Item, Boolean>>,
)
