import heuristics.MDSFLA
import provider.JohnBurkardt
import kotlin.random.Random

fun main() {
    val problem = JohnBurkardt.getProblem(7)
    println(problem)

    val solution = MDSFLA(5, 5, 10, 10, Random(2023)).solve(problem)
    println(solution)
}
