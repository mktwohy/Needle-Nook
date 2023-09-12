package com.mktwohy.knittingcalculator.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    count: Int,
    isIncrementEnabled: Boolean,
    isDecrementEnabled: Boolean,
    isResetEnabled: Boolean,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
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
        Counter(
            count = count,
            onIncrement = onIncrement,
            onDecrement = onDecrement,
            isIncrementEnabled = isIncrementEnabled,
            isDecrementEnabled = isDecrementEnabled
        )
        Button(
            onClick = onReset,
            enabled = isResetEnabled,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = ButtonDefaults.filledTonalShape)
                .height(ButtonDefaults.MinHeight)
        ) {
            Text("Reset")
        }
    }

}

@Composable
private fun Counter(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    isIncrementEnabled: Boolean,
    isDecrementEnabled: Boolean,
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
            enabled = isDecrementEnabled,
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
            enabled = isIncrementEnabled,
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
            count = 0,
            onIncrement = { },
            onDecrement = { },
            onReset = { },
            isIncrementEnabled = true,
            isDecrementEnabled = false,
            isResetEnabled = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}