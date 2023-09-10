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
import com.mktwohy.knittingcalculator.composables.App
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
            val count by viewModel.count.collectAsState()
            val stitchCount by viewModel.stitchCount.collectAsState(initial = "")
            val showResetDialog by viewModel.showResetDialog.collectAsState()

            KnittingCalculatorTheme {
                App(
                    densityInput = viewModel.density,
                    lengthInput = viewModel.length,
                    stitchOutput = stitchCount,
                    count = count,
                    onClickCountDecrement = viewModel::decrementCounter,
                    onClickCountIncrement = viewModel::incrementCounter,
                    onClickCountReset = viewModel::onClickReset,
                )
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
