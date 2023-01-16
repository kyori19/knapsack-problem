package reports

import java.io.OutputStream

fun <K, S : Any> Report(fn: (MutableReport<K, S>).() -> S): Report<K, S> = MutableReport<K, S>().apply { submit(fn()) }

interface Report<K, S : Any> {
    val history: Map<K, Int>
    val solution: S
    fun writeCSV(stream: OutputStream)
}

class MutableReport<K, S : Any> : Report<K, S> {
    override val history = mutableMapOf<K, Int>()
    override lateinit var solution: S

    fun report(key: K, value: Int) {
        history[key] = value
    }

    internal fun submit(s: S) {
        solution = s
    }

    override fun writeCSV(stream: OutputStream) {
        stream.bufferedWriter().apply {
            history.forEach { (k, v) ->
                appendLine("$k, $v")
            }
            flush()
        }
    }

    override fun toString() = buildString {
        append("Report[")
        runCatching { solution }.onSuccess {
            append("solution=$solution, ")
        }
        append("history=$history")
        append("]")
    }
}
