package knapsack

data class KnapsackProblem(
        val capacity: Int,
        val optimal: List<Pair<Item, Boolean>>,
) {
    val items = optimal.map(Pair<Item, *>::first)

    private val optimalProfit = optimal.filter { it.second }.sumOf { it.first.profit }

    override fun toString(): String = "KnapsackProblem(capacity=$capacity, optimalProfit=$optimalProfit, items=$items)"

    inner class Solution {
        val problem = this@KnapsackProblem

        private val _selection = mutableSetOf<Item>()
        val selection: MutableSet<Item> = object : MutableSet<Item> by _selection {
            override fun add(element: Item): Boolean = element in items && _selection.add(element)

            override fun addAll(elements: Collection<Item>): Boolean = all { (it in items) } && elements.isEmpty() || _selection.addAll(elements)

            override fun toString(): String = _selection.toString()
        }

        val weight: Int
            get() = selection.sumOf { it.weight }

        val profit: Int
            get() = selection.sumOf { it.profit }

        val valid: Boolean
            get() = weight <= capacity

        operator fun contains(item: Item): Boolean = item in selection

        override fun toString(): String = "Solution(weight=$weight/$capacity, profit=$profit, valid=$valid, selection=$selection)"
    }
}
