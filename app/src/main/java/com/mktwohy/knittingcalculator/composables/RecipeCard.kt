package com.mktwohy.knittingcalculator.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mktwohy.knittingcalculator.Input

@Composable
fun FormulaCard(
    title: String,
    inputs: List<Input<String>>,
    output: String,
    onClickImeDone: (KeyboardActionScope.() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
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
