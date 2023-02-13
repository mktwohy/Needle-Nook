package com.mktwohy.knittingcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mktwohy.knittingcalculator.ui.theme.KnittingCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel by viewModels()
        setContent {
            KnittingCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        FormulaCard(
                            title = "Number of Stitches",
                            inputs = listOf(viewModel.density, viewModel.length),
                            output = viewModel.stitchCount
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
) {
    Box(
        Modifier
            .background(
                color = Color.DarkGray,
                shape = RoundedCornerShape(8.dp)
            )
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
                for (input in inputs) {
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
                            keyboardType = KeyboardType.Decimal
                        )
                    )
                }
            }
            Text(output)
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