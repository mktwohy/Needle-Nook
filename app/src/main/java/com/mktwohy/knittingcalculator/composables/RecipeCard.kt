package com.mktwohy.knittingcalculator.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mktwohy.knittingcalculator.FormulaInputUiState
import com.mktwohy.knittingcalculator.FormulaUiState

@Composable
fun FormulaCard(
    uiState: FormulaUiState,
    onInputTextChange: (index: Int, value: String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                shape = MaterialTheme.shapes.medium
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
                text = uiState.outputLabel,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                uiState.inputs.forEachIndexed { index, inputUiState ->
                    FormulaTextField(
                        uiState = inputUiState,
                        onTextChange = { onInputTextChange(index, it) },
                        isLastInput = index == uiState.inputs.lastIndex,
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
                    text = uiState.output,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun FormulaTextField(
    uiState: FormulaInputUiState,
    onTextChange: (String) -> Unit,
    isLastInput: Boolean
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = uiState.text,
        onValueChange = onTextChange,
        label = { Text(uiState.label) },
        placeholder = { Text(uiState.label) },
        suffix = uiState.unit?.let {
            { Text(text = it) }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.background,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = if (isLastInput) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}
