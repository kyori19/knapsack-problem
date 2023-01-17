import heuristics.MDSFLA
import heuristics.OWOA
import provider.JohnBurkardt
import java.io.File
import kotlin.random.Random

fun main() {
    val problem = JohnBurkardt.getProblem(8)
    println(problem)

    run {
        val report = MDSFLA(10, 10, 10, 500, Random(2023)).solve(problem)
        println(report.solution)
        report.writeCSV(File("MDSFLA.csv").outputStream())
    }

    run {
        val report = OWOA(25, 5000, 1.0, Random(2023)).solve(problem)
        println(report.solution)
        report.writeCSV(File("OWOA.csv").outputStream())
    }
}
