package com.mktwohy.knittingcalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(private val repository: Repository) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY]
                MainViewModel((app as KnittingApplication).repository)
            }
        }
    }

    private val _numberOfStitchesUiState = MutableStateFlow(NumberOfStitchesFormulaUiState())
    val numberOfStitchesUiState = _numberOfStitchesUiState.asStateFlow()

    fun onNumberOfStitchesInputChange(index: Int, value: String) {
        _numberOfStitchesUiState.update { uiState ->
            when (index) {
                0 -> uiState.copy(densityText = value)
                1 -> uiState.copy(lengthText = value)
                else -> error("Invalid formula input index: $index")
            }
        }
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

data class NumberOfStitchesFormulaUiState(
    val densityText: String = "",
    val lengthText: String = ""
) : FormulaUiState {
    override val outputLabel: String = "Number of Stitches"
    override val inputs: List<FormulaInputUiState> = listOf(
        FormulaInputUiState(
            text = densityText,
            label = "Density",
            unit = "Stitch/Inch",
            error = null, // TODO
        ),
        FormulaInputUiState(
            text = lengthText,
            label = "Length",
            unit = "Inch",
            error = null // TODO
        )
    )
    override val output: String = densityText.toFloatOrNull()?.let { d ->
        lengthText.toFloatOrNull()?.let { l ->
            d * l
        }
    }?.toString() ?: ""
}

interface FormulaUiState {
    val outputLabel: String
    val inputs: List<FormulaInputUiState>
    val output: String
}

data class FormulaInputUiState(
    val text: String = "",
    val label: String = "",
    val unit: String? = null,
    val error: String? = "",
)
