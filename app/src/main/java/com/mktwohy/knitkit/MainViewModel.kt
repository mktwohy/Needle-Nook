package com.mktwohy.knitkit

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

    private val _stitchCounterUiState = MutableStateFlow(StitchCounterUiState(stitchCount = repository.count))
    val stitchCounterUiState = _stitchCounterUiState.asStateFlow()

    fun onNumberOfStitchesInputChange(index: Int, value: String) {
        _numberOfStitchesUiState.update { uiState ->
            when (index) {
                0 -> uiState.copy(densityText = value)
                1 -> uiState.copy(lengthText = value)
                else -> error("Invalid formula input index: $index")
            }
        }
    }

    fun incrementCounter() {
        _stitchCounterUiState.update { uiState ->
            uiState.copy(stitchCount = uiState.stitchCount + 1)
        }
        saveState()
    }

    fun decrementCounter() {
        _stitchCounterUiState.update { uiState ->
            uiState.copy(stitchCount = uiState.stitchCount - 1)
        }
        saveState()
    }

    fun onClickReset() {
        _stitchCounterUiState.update { uiState ->
            check(uiState.isResetButtonEnabled)
            uiState.copy(showResetDialog = true)
        }
    }

    fun onConfirmReset() {
        _stitchCounterUiState.update { uiState ->
            check(uiState.isResetButtonEnabled)
            uiState.copy(stitchCount = 0, showResetDialog = false)
        }
    }

    fun onCancelReset() {
        _stitchCounterUiState.update { uiState ->
            check(uiState.isResetButtonEnabled)
            uiState.copy(showResetDialog = false)
        }
    }

    fun saveState() {
        repository.count = this.stitchCounterUiState.value.stitchCount
    }
}

data class StitchCounterUiState(
    val stitchCount: Int = 0,
    val showResetDialog: Boolean = false
) {
    val isResetButtonEnabled: Boolean = stitchCount > 0
    val isDecrementButtonEnabled: Boolean = stitchCount > 0
    val isIncrementButtonEnabled: Boolean = true
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
