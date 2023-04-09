package com.mktwohy.knittingcalculator

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainViewModel(private val repository: Repository) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY]
                MainViewModel((app as KnittingApplication).repository)
            }
        }
    }

    var density = Input(initValue = "", name = "Density", unit = "Stitch/Inch")
    var length = Input(initValue = "", name = "Length", unit = "Inch")
    val stitchCount by derivedStateOf {
        val density = this.density.value.toFloatOrNull() ?: return@derivedStateOf ""
        val length = this.length.value.toFloatOrNull() ?: return@derivedStateOf ""
        getStitchCount(density, length).toString()
    }
    var count by mutableStateOf(repository.count)
    var showResetDialog by mutableStateOf(false)

    fun saveState() {
        repository.count = this.count
    }
}
