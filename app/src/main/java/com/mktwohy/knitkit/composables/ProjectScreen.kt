package com.mktwohy.knitkit.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mktwohy.knitkit.MainViewModel
import com.mktwohy.knitkit.Repository
import com.mktwohy.knitkit.ui.theme.KnittingCalculatorTheme

@Composable
fun ProjectScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val stitchCounterUiState by viewModel.stitchCounterUiState.collectAsState()

    Column(modifier) {
        StitchCounter(
            count = stitchCounterUiState.stitchCount,
            onDecrement = viewModel::decrementCounter,
            onIncrement = viewModel::incrementCounter,
            onReset = viewModel::onClickReset,
            isIncrementEnabled = stitchCounterUiState.isIncrementButtonEnabled,
            isDecrementEnabled = stitchCounterUiState.isDecrementButtonEnabled,
            isResetEnabled = stitchCounterUiState.isResetButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
    AlertDialog(
        show = stitchCounterUiState.showResetDialog,
        title = "Confirm Reset",
        message = "Are you sure you want to reset? Count will be lost.",
        confirm = "OK",
        dismiss = "Cancel",
        onConfirm = viewModel::onConfirmReset,
        onDismiss = viewModel::onCancelReset
    )
}

@Preview(name = "Light Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    KnittingCalculatorTheme {
        ProjectScreen(
            viewModel = MainViewModel(repository = Repository(LocalContext.current)),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}