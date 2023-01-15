package knapsack

data class Item(
    val weight: Int,
    val profit: Int,
) {
    val performance = profit / weight
}
