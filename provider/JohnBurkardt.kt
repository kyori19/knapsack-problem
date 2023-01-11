package provider

import knapsack.Item
import knapsack.KnapsackProblem
import java.net.URL

object JohnBurkardt {
    private const val baseUrl = "https://people.sc.fsu.edu/~jburkardt/datasets/knapsack_01/"

    fun getProblem(id: Int) = KnapsackProblem(
            fetch(buildUrl(id, 'c'))[0],
            createItems(id),
    )

    private fun createItems(id: Int) =
            fetch(buildUrl(id, 'w')).zip(fetch(buildUrl(id, 'p'))) { w, p -> Item(w, p) }
                    .zip(fetch(buildUrl(id, 's')).map { it == 1 }) { i, o -> i to o }

    private fun buildUrl(id: Int, type: Char) =
            URL("${baseUrl}p${id.toString().padStart(2, '0')}_$type.txt")

    private fun fetch(url: URL) =
            url.openConnection().getInputStream().bufferedReader().readLines().map(String::trim).map(String::toInt)
}
