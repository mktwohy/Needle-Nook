package com.mktwohy.knittingcalculator

import kotlinx.coroutines.flow.*

class InputState<T>(
    initValue: T,
    val name: String,
    val unit: String
) {
    private val _input = MutableStateFlow(initValue)
    val input = _input.asStateFlow()
    val onInputChange: (T) -> Unit = { _input.value = it }
}

fun <T1, T2, R> InputState<T1>.combine(
    other: InputState<T2>,
    transform: suspend (a: T1, b: T2) -> R
): Flow<R> =
    this.input.combine(other.input, transform)