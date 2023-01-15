import heuristics.MDSFLA
import heuristics.OWOA
import provider.JohnBurkardt
import kotlin.random.Random

fun main() {
    val problem = JohnBurkardt.getProblem(7)
    println(problem)

    run {
        val solution = MDSFLA(5, 5, 10, 100, Random(2023)).solve(problem)
        println(solution)
    }

    run {
        val solution = OWOA(25, 1000, 1.0, Random(2023)).solve(problem)
        println(solution)
    }
}
