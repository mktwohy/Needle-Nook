package com.mktwohy.knittingcalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class MainViewModel(private val repository: Repository) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY]
                MainViewModel((app as KnittingApplication).repository)
            }
        }
    }

    val density = InputState(initValue = "", name = "Density", unit = "Stitch/Inch")
    val length = InputState(initValue = "", name = "Length", unit = "Inch")
    val stitchCount = density.combine(length) { density, length ->
        density.toFloatOrNull()?.let { d ->
            length.toFloatOrNull()?.let { l ->
                getStitchCount(d, l).toString()
            }
        } ?: ""
    }

    private val _count = MutableStateFlow(repository.count)
    val count = _count.asStateFlow()

    private val _showResetDialog = MutableStateFlow(false)
    val showResetDialog = _showResetDialog.asStateFlow()

    fun incrementCounter() {
        _count.value += 1
    }

    fun decrementCounter() {
        _count.value -= 1
    }

    fun onClickReset() {
        if (count.value > 0) {
            _showResetDialog.value = true
        }
    }

    fun onConfirmReset() {
        _count.value = 0
        _showResetDialog.value = false
    }

    fun onCancelReset() {
        _showResetDialog.value = false
    }

    fun saveState() {
        repository.count = this.count.value
    }
}
