package com.mktwohy.knittingcalculator.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mktwohy.knittingcalculator.ui.theme.KnittingCalculatorTheme

@Composable
fun StitchCounter(
    modifier: Modifier = Modifier,
    count: Int,
    onIncrement: () -> Unit,
    onDecremenet: () -> Unit,
    onReset: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp)
    ) {
        Counter(count, onIncrement, onDecremenet)
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
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = ButtonDefaults.shape
            )
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
                .background(MaterialTheme.colorScheme.tertiaryContainer, itemShape)
                .width(ButtonDefaults.MinWidth)
                .height(ButtonDefaults.MinHeight)
        ) {
            Text(
                text = count.toString(),
                color = MaterialTheme.colorScheme.onTertiaryContainer
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

@Preview(name = "Dark Theme", showBackground = true, device = Devices.PIXEL_3A, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Theme", showBackground = true, device = Devices.PIXEL_3A)
@Composable
private fun StitchCounterPreview() {
    KnittingCalculatorTheme {
        StitchCounter(
            count = 1,
            onIncrement = { },
            onDecremenet = { },
            onReset = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}