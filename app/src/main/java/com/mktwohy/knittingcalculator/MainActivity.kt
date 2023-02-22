package com.mktwohy.knittingcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mktwohy.knittingcalculator.composables.Counter
import com.mktwohy.knittingcalculator.composables.FormulaCard
import com.mktwohy.knittingcalculator.extensions.noRippleClickable
import com.mktwohy.knittingcalculator.ui.theme.KnittingCalculatorTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Repository.init(this)

        val viewModel: MainViewModel by viewModels()
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
                            onCountChange = {
                                viewModel.count = it
                                viewModel.saveState()
                            },
                            range = 0..Int.MAX_VALUE,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
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