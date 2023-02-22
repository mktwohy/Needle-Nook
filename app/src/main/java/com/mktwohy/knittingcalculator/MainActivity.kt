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
                        Counter(
                            count = viewModel.count,
                            onCountChange = {
                                viewModel.count = it
                                viewModel.saveState()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FormulaCard(
    title: String,
    inputs: List<Input<String>>,
    output: String,
    onClickImeDone: (KeyboardActionScope.() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                inputs.forEachIndexed { index, input ->
                    FormulaTextField(
                        input = input,
                        isLastInput = index == inputs.lastIndex,
                        onClickImeDone = onClickImeDone
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(TextFieldDefaults.MinWidth)
                    .height(TextFieldDefaults.MinHeight)
            ) {
                Text(
                    text = output,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun FormulaTextField(
    input: Input<String>,
    isLastInput: Boolean,
    onClickImeDone: (KeyboardActionScope.() -> Unit)?
) {
    OutlinedTextField(
        value = input.value,
        onValueChange = input.onValueChange,
        trailingIcon = {
            Text(
                text = input.unit,
                modifier = Modifier.padding(end = 4.dp)
            )
        },
        label = { Text(input.name) },
        placeholder = { Text(input.name) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = if (isLastInput) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onDone = onClickImeDone)
    )
}

@Composable
fun Counter(count: Int, onCountChange: (Int) -> Unit) {
    Row {
        Button(onClick = { onCountChange(count - 1) }) {
            Text(text = "-")
        }
        Text(text = count.toString())
        Button(onClick = { onCountChange(count + 1) }) {
            Text(text = "+")
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