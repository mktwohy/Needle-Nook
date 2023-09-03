package com.mktwohy.knittingcalculator.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mktwohy.knittingcalculator.ui.theme.KnittingCalculatorTheme

@Composable
fun StitchCounter(
    modifier: Modifier = Modifier,
    count: Int,
    onClickIncrement: () -> Unit,
    onClickDecrement: () -> Unit,
    onReset: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(4.dp)
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Counter(count, onClickIncrement, onClickDecrement)
        Button(onClick = onReset) {
            Text("Reset")
        }
    }

}

@Composable
private fun Counter(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val itemShape = remember { RoundedCornerShape(2.dp) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onDecrement,
            modifier = Modifier
                .height(ButtonDefaults.MinHeight)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Increment"
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.White, itemShape)
                .border(1.dp, Color.Black, itemShape)
                .width(ButtonDefaults.MinWidth)
                .height(ButtonDefaults.MinHeight)
        ) {
            Text(
                text = count.toString(),
                color = Color.Black
            )
        }
        Button(
            onClick = onIncrement,
            modifier = Modifier
                .height(ButtonDefaults.MinHeight)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increment"
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
private fun DefaultPreview() {
    KnittingCalculatorTheme {
        Box(Modifier.fillMaxSize()) {
            StitchCounter(
                count = 1,
                onClickIncrement = { },
                onClickDecrement = { },
                onReset = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }
    }
}