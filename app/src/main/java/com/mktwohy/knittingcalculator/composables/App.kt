package com.mktwohy.knittingcalculator.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.mktwohy.knittingcalculator.InputState
import com.mktwohy.knittingcalculator.extensions.noRippleClickable
import com.mktwohy.knittingcalculator.ui.theme.KnittingCalculatorTheme

@Composable
fun App(
    densityInput: InputState<String>,
    lengthInput: InputState<String>,
    stitchOutput: String,
    count: Int,
    onClickCountIncrement: () -> Unit,
    onClickCountDecrement: () -> Unit,
    onClickCountReset: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickable { focusManager.clearFocus() }
    ) {
        Column {
            FormulaCard(
                title = "Number of Stitches",
                inputStates = listOf(densityInput, lengthInput),
                output = stitchOutput,
                onClickImeDone = { focusManager.clearFocus() }
            )
            Spacer(Modifier.weight(1f)) // height and background only for demonstration
            StitchCounter(
                count = count,
                onClickDecrement = onClickCountIncrement,
                onClickIncrement = onClickCountDecrement,
                onReset = onClickCountReset,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
private fun Preview() {
    KnittingCalculatorTheme {
        App(
            count = 0,
            onClickCountDecrement = {},
            onClickCountIncrement = {},
            onClickCountReset = {},
            densityInput = InputState(initValue = "", name = "Density", unit = "Unit"),
            lengthInput = InputState(initValue = "", name = "Length", unit ="Unit"),
            stitchOutput = "",
        )
    }
}