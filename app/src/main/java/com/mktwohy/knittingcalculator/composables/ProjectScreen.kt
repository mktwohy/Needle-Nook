package com.mktwohy.knittingcalculator.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.mktwohy.knittingcalculator.MainViewModel
import com.mktwohy.knittingcalculator.Repository
import com.mktwohy.knittingcalculator.ui.theme.KnittingCalculatorTheme

@Composable
fun ProjectScreen(
    count: Int,
    onCountDecrement: () -> Unit,
    onCountIncrement: () -> Unit,
    onCountReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        StitchCounter(
            count = count,
            onDecremenet = onCountDecrement,
            onIncrement = onCountIncrement,
            onReset = onCountReset,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(name = "Light Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    KnittingCalculatorTheme {
        ProjectScreen(
            count = 0,
            onCountDecrement = { },
            onCountIncrement = { },
            onCountReset = { },
            modifier = Modifier
                .fillMaxSize()
        )
    }
}