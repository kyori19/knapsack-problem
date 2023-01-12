package utils

fun <E> MutableList<E>.toReplaceableDiscreteList(step: Int) = ReplaceableDiscreteList(this, step)

class ReplaceableDiscreteList<E>(
    private val source: MutableList<E>,
    private val groupCount: Int,
) : Collection<ReplaceableCollection<E>> by List(groupCount, { groupNo ->
    fun getList(): List<E> = source.filterIndexed { i, _ -> i % groupCount == groupNo }

    object : ReplaceableCollection<E> {

        override val size: Int
            get() = getList().size

        override fun isEmpty(): Boolean = getList().isEmpty()

        override fun iterator(): Iterator<E> = getList().iterator()

        override fun containsAll(elements: Collection<E>): Boolean = getList().containsAll(elements)

        override fun contains(element: E): Boolean = getList().contains(element)

        override fun replace(old: E, new: E): E = source.set(source.indexOf(old), new)
    }
})
