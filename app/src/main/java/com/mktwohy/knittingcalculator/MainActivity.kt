package com.mktwohy.knittingcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.mktwohy.knittingcalculator.composables.AlertDialog
import com.mktwohy.knittingcalculator.composables.StitchCounter
import com.mktwohy.knittingcalculator.composables.FormulaCard
import com.mktwohy.knittingcalculator.extensions.noRippleClickable
import com.mktwohy.knittingcalculator.ui.theme.KnittingCalculatorTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        setContent {
            val focusManager = LocalFocusManager.current
            val count by viewModel.count.collectAsState()
            val stitchCount by viewModel.stitchCount.collectAsState(initial = "")
            val showResetDialog by viewModel.showResetDialog.collectAsState()

            KnittingCalculatorTheme {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier
                        .fillMaxSize()
                        .noRippleClickable { focusManager.clearFocus() }
                ) {
                    Column {
                        FormulaCard(
                            title = "Number of Stitches",
                            inputStates = listOf(viewModel.density, viewModel.length),
                            output = stitchCount,
                            onClickImeDone = { focusManager.clearFocus() }
                        )
                        Spacer(Modifier.weight(1f)) // height and background only for demonstration
                        StitchCounter(
                            count = count,
                            onClickDecrement = viewModel::decrementCounter,
                            onClickIncrement = viewModel::incrementCounter,
                            onReset = viewModel::onClickReset,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                AlertDialog(
                    show = showResetDialog,
                    title = "Confirm Reset",
                    message = "Are you sure you want to reset? Count will be lost.",
                    confirm = "OK",
                    dismiss = "Cancel",
                    onConfirm = viewModel::onConfirmReset,
                    onDismiss = viewModel::onCancelReset
                )
            }
        }
    }

    override fun onPause() {
        viewModel.saveState()
        super.onPause()
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun DefaultPreview() {
    KnittingCalculatorTheme {
        FormulaCard(
            title = "Formula Title",
            inputStates = listOf(
                InputState(initValue = "0.0", name = "Input 1", unit = "Unit"),
                InputState(initValue = "", name = "Input 2", unit = "Unit"),
            ),
            output = "3.0"
        )
    }
}