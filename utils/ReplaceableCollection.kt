package utils

interface ReplaceableCollection<E> : Collection<E> {
    fun replace(old: E, new: E): E
}
