package com.mktwohy.needlenook.ui.composables.projectscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.FormatItalic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

fun AnnotatedString.getSpanStyles(start: Int, end: Int): List<AnnotatedString.Range<SpanStyle>> =
    this.spanStyles.filter { start >= it.start && end <= it.end }

fun TextFieldValue.getSelectedSpanStyles(): List<AnnotatedString.Range<SpanStyle>> =
    annotatedString.getSpanStyles(selection.start, selection.end)

fun TextFieldValue.annotateAsMarkdown(): TextFieldValue =
    this.copy(annotatedString = Markdown(text).toAnnotatedString())

@Composable
fun MarkdownEditor(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = MaterialTheme.shapes.medium


    Surface(
        shape = shape,
        tonalElevation = 4.dp,
        modifier = modifier
    ) {
        Column(Modifier.padding(8.dp)) {
            MarkdownEditorButtonRow(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .background(
                        shape = shape,
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                    )
                    .fillMaxSize()
            ) {
                BasicMarkdownTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun BasicMarkdownTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier
) {
    LaunchedEffect(Unit) {
        onValueChange(value.annotateAsMarkdown())
    }

    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it.annotateAsMarkdown()) },
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        modifier = modifier
    )
}

@Composable
private fun MarkdownEditorButtonRow(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier
) {
    val selectedSpanStyles = value.getSelectedSpanStyles()
    val isSelectionBold = selectedSpanStyles.any { it.item.fontWeight == FontWeight.Bold }
    val isSelectionItalic = selectedSpanStyles.any { it.item.fontStyle == FontStyle.Italic }

    Row(modifier = modifier) {
        MarkdownStyleButton(
            onClick = { /*TODO*/ },
            icon = Icons.Outlined.FormatBold,
            isSelected = isSelectionBold
        )
        MarkdownStyleButton(
            onClick = { /*TODO*/ },
            icon = Icons.Outlined.FormatItalic,
            isSelected = isSelectionItalic
        )
    }
}

@Composable
private fun MarkdownStyleButton(
    onClick: () -> Unit,
    icon: ImageVector,
    isSelected: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme

    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = if (isSelected) colorScheme.primary else colorScheme.onSurface,
            containerColor = if (isSelected) colorScheme.inversePrimary else Color.Transparent
        )
    ) {
        Icon(imageVector = icon, contentDescription = icon.name)
    }
}

@Preview
@Composable
private fun MarkdownEditorPreview() {
    var text by remember {
        mutableStateOf(
            TextFieldValue(
                """
                    ## Header 2
                    Plain Text
                    - *Italics*
                    - **Bold**
                      1. ***Bold and Italics***
                      2. `code`
                """.trimIndent()
            )
        )
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        MarkdownEditor(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        )
    }
}
