package com.mktwohy.knittingcalculator

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    object StitchesPerInch {
        var density by mutableStateOf("")
        var length by mutableStateOf("")
        val stitchCount by derivedStateOf {
            val density = this.density.toFloatOrNull() ?: return@derivedStateOf null
            val length = this.length.toFloatOrNull() ?: return@derivedStateOf null
            getStitchCount(density, length).toString()
        }
    }
}
