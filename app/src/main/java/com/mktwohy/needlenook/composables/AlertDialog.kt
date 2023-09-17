package com.mktwohy.needlenook.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AlertDialog(
    title: String,
    message: String,
    confirm: String,
    dismiss: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    cancelable: Boolean = true
) {
    AlertDialog(
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirm)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismiss)
            }
        },
        onDismissRequest = { if (cancelable) onDismiss() },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun AlertDialog(
    title: String,
    content: @Composable () -> Unit,
    confirm: String,
    dismiss: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    cancelable: Boolean = true
) {
    AlertDialog(
        title = { Text(title) },
        text = content,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirm)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismiss)
            }
        },
        onDismissRequest = { if (cancelable) onDismiss() },
        containerColor = MaterialTheme.colorScheme.surface
    )
}