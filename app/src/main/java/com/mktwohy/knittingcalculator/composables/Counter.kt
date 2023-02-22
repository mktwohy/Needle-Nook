package com.mktwohy.knittingcalculator.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

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