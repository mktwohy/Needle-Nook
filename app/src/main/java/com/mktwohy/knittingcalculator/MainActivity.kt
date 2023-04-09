package com.mktwohy.knittingcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.mktwohy.knittingcalculator.composables.AlertDialog
import com.mktwohy.knittingcalculator.composables.Counter
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
                            inputs = listOf(viewModel.density, viewModel.length),
                            output = viewModel.stitchCount,
                            onClickImeDone = { focusManager.clearFocus() }
                        )
                        Spacer(Modifier.weight(1f)) // height and background only for demonstration
                        Counter(
                            count = viewModel.count,
                            onCountChange = { viewModel.count = it },
                            onReset = {
                                if (viewModel.count > 0) {
                                    viewModel.showResetDialog = true
                                }
                            },
                            range = 0..Int.MAX_VALUE,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                AlertDialog(
                    show = viewModel.showResetDialog,
                    title = "Confirm Reset",
                    message = "Are you sure you want to reset? Count will be lost.",
                    confirm = "OK",
                    dismiss = "Cancel",
                    onConfirm = {
                        viewModel.count = 0
                        viewModel.saveState()
                        viewModel.showResetDialog = false
                    },
                    onDismiss = { viewModel.showResetDialog = false }
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
            inputs = listOf(
                Input(initValue = "0.0", name = "Input 1", unit = "Unit"),
                Input(initValue = "", name = "Input 2", unit = "Unit"),
            ),
            output = "3.0"
        )
    }
}