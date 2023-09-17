package com.mktwohy.needlenook.extensions

inline fun <T> List<T>.mutate(transform: MutableList<T>.() -> Unit): List<T> =
    this.toMutableList().apply(transform).toList()

inline fun <T> MutableList<T>.replaceIf(
    crossinline predicate: (T) -> Boolean,
    crossinline transform: (T) -> T
) {
    replaceAll {
        if (predicate(it)) transform(it) else it
    }
}