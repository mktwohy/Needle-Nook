package com.mktwohy.needlenook.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FormulaScreenViewModel : ViewModel() {
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
