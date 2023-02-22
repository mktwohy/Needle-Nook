package com.mktwohy.knittingcalculator

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var density = Input(initValue = "", name = "Density", unit = "Stitch/Inch")
    var length = Input(initValue = "", name = "Length", unit = "Inch")
    val stitchCount by derivedStateOf {
        val density = this.density.value.toFloatOrNull() ?: return@derivedStateOf ""
        val length = this.length.value.toFloatOrNull() ?: return@derivedStateOf ""
        getStitchCount(density, length).toString()
    }
    var count by mutableStateOf(Repository.count)
    var showResetDialog by mutableStateOf(false)

    fun saveState() {
        Repository.count = this.count
    }
}
