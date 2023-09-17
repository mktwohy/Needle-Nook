package com.mktwohy.needlenook.extensions

inline fun <T> List<T>.mutate(transform: MutableList<T>.() -> Unit): List<T> =
    this.toMutableList().apply(transform).toList()