package com.mktwohy.knittingcalculator

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Input<T>(
    initValue: T,
    val name: String,
    val unit: String
) {
    val value by derivedStateOf { state }
    val onValueChange: (T) -> Unit = { state = it }
    private var state by mutableStateOf(initValue)
}
