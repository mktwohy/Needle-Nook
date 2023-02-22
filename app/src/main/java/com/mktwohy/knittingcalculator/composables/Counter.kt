package com.mktwohy.knittingcalculator.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mktwohy.knittingcalculator.Input
import com.mktwohy.knittingcalculator.ui.theme.KnittingCalculatorTheme

@Composable
fun Counter(
    modifier: Modifier = Modifier,
    count: Int,
    onCountChange: (Int) -> Unit,
    onReset: () -> Unit,
    range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE,
) {
    val itemShape = remember { RoundedCornerShape(2.dp) }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(4.dp)
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onCountChange((count - 1).coerceIn(range)) }
            ) {
                Text(text = "-")
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
                onClick = { onCountChange((count + 1).coerceIn(range)) }
            ) {
                Text(text = "+")
            }
        }

        Button(onClick = onReset) {
            Text("Reset")
        }
    }

}

@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
private fun DefaultPreview() {
    KnittingCalculatorTheme {
        Counter(
            count = 1,
            onCountChange = { },
            onReset = { }
        )
    }
}