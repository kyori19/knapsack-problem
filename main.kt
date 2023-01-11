import provider.JohnBurkardt
import utils.ensureSuccess

fun main() {
    val problem = JohnBurkardt.getProblem(1)
    println(problem)

    problem.run {
        val solution = Solution()
        solution.selection.addAll(items.take(4)).ensureSuccess()
        println(solution)
    }
}
