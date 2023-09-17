package com.mktwohy.needlenook.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mktwohy.needlenook.MainViewModel
import com.mktwohy.needlenook.ui.theme.NeedleNookTheme

@Composable
fun FormulaScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val numberOfStitchesFormulaUiState by viewModel.numberOfStitchesUiState.collectAsState()

    Column(modifier) {
        FormulaCard(
            uiState = numberOfStitchesFormulaUiState,
            onInputTextChange = viewModel::onNumberOfStitchesInputChange,
        )
    }
}

@Preview(name = "Light Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    NeedleNookTheme {
        FormulaScreen(
            viewModel = MainViewModel(),
            modifier = Modifier.fillMaxSize()
        )
    }
}