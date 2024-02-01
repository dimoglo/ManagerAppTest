package net.nomia.core.common.utils

fun <T> Iterable<T>.getDifference(elements: Collection<T>): List<T> = this.minus(elements.toSet())

fun <T, K> Iterable<T>.getDifference(elements: Collection<T>, keySelector: (T) -> K): List<T> {
    if (elements.isEmpty()) return this.toList()
    return this.filterNot { keySelector(it) in elements.map(keySelector) }
}

fun <T> Iterable<T>.getSimilar(elements: Collection<T>): List<T> {
    if (elements.isEmpty()) return emptyList()
    return this.filter { it in elements }
}

fun <T, K> Iterable<T>.getSimilar(elements: Collection<T>, keySelector: (T) -> K): List<Pair<T, T>> {
    if (elements.isEmpty()) return emptyList()
    return this
        .filter { keySelector(it) in elements.map(keySelector) }
        .map { item ->
            item to elements.find { keySelector(item) == keySelector(it) }!!
        }
}
