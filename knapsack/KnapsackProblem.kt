package knapsack

data class KnapsackProblem(
        val capacity: Int,
        val optimal: List<Pair<Item, Boolean>>,
) {
    val items = optimal.map(Pair<Item, *>::first)

    inner class Solution {
        private val _selection = mutableSetOf<Item>()
        val selection: MutableSet<Item> = object : MutableSet<Item> by _selection {
            override fun add(element: Item): Boolean = element in items && _selection.add(element)

            override fun addAll(elements: Collection<Item>): Boolean = all { (it in items) } && _selection.addAll(elements)

            override fun toString(): String = _selection.toString()
        }

        private val weight: Int
            get() = selection.sumOf { it.weight }

        private val profit: Int
            get() = selection.sumOf { it.profit }

        private val valid: Boolean
            get() = weight <= capacity

        override fun toString(): String = "Solution(weight=$weight/$capacity, profit=$profit, valid=$valid, selection=$selection)"
    }
}
