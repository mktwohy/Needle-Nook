package com.mktwohy.knittingcalculator.composables

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun AlertDialog(
    show: Boolean,
    title: String,
    message: String,
    confirm: String,
    dismiss: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    cancelable: Boolean = true
) {
    if (show) {
        AlertDialog(
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                ) {
                    Text(text = confirm)
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss
                ) {
                    Text(text = dismiss)
                }
            },
            onDismissRequest = { if (cancelable) onDismiss() },
            backgroundColor = MaterialTheme.colors.surface
        )
    }
}