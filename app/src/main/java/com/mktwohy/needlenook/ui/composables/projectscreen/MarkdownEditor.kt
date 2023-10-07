package com.mktwohy.needlenook.ui.composables.projectscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.FormatIndentDecrease
import androidx.compose.material.icons.outlined.FormatIndentIncrease
import androidx.compose.material.icons.outlined.FormatItalic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.unit.dp
import com.mktwohy.needlenook.ui.composables.VerticalDivider

fun AnnotatedString.getSpanStyles(start: Int, end: Int): List<AnnotatedString.Range<SpanStyle>> =
    this.spanStyles.filter { start >= it.start && end <= it.end }

fun TextFieldValue.getSelectedSpanStyles(): List<AnnotatedString.Range<SpanStyle>> =
    annotatedString.getSpanStyles(selection.start, selection.end)

fun TextFieldValue.annotateAsMarkdown(): TextFieldValue =
    this.copy(annotatedString = Markdown(text).toAnnotatedString())

fun TextFieldValue.applyMdStyleToSelection(mdStyle: String): TextFieldValue {
    val originalTextLength = this.text.length
    val cursorOffset = mdStyle.length
    return this.copy(
        annotatedString = buildAnnotatedString {
            append(getTextBeforeSelection(maxChars = originalTextLength))
            append(mdStyle)
            append(getSelectedText())
            append(mdStyle)
            append(getTextAfterSelection(maxChars = originalTextLength))
        },
        selection = TextRange(
            start = selection.start + cursorOffset,
            end = selection.end + cursorOffset
        )
    ).annotateAsMarkdown()
}

fun TextFieldValue.removeMdStyleFromSelection(mdStyle: String): TextFieldValue {
    val originalTextLength = this.text.length
    val cursorOffset = mdStyle.length
    val textBeforeSelection = getTextBeforeSelection(maxChars = originalTextLength)
    val textAfterSelection = getTextAfterSelection(maxChars = originalTextLength)

    if (!textBeforeSelection.endsWith(mdStyle) || !textAfterSelection.startsWith(mdStyle)) {
        return this
    }
    return this.copy(
        annotatedString = buildAnnotatedString {
            append(textBeforeSelection.removeSuffix(mdStyle))
            append(getSelectedText())
            append(textAfterSelection.removePrefix(mdStyle))
        },
        selection = TextRange(
            start = selection.start - cursorOffset,
            end = selection.end - cursorOffset
        )
    ).annotateAsMarkdown()
}

fun TextFieldValue.increaseSelectedLineIndent(): TextFieldValue {
    val originalTextLength = this.text.length

    val textBeforeSelection = getTextBeforeSelection(maxChars = originalTextLength)
    val textAfterSelection = getTextAfterSelection(maxChars = originalTextLength)
    val selectedLineStartIndex = textBeforeSelection.indexOfLast { it == '\n' } + 1

    return this.copy(
        annotatedString = buildAnnotatedString {
            append(textBeforeSelection.slice(0..<selectedLineStartIndex))
            append(" ".repeat(4))
            append(textBeforeSelection.slice(selectedLineStartIndex..textBeforeSelection.lastIndex))
            append(getSelectedText())
            append(textAfterSelection)
        },
        selection = TextRange(
            start = selection.start + 4,
            end = selection.end + 4
        )
    ).annotateAsMarkdown()
}

fun TextFieldValue.decreaseSelectedLineIndent(): TextFieldValue {
    val originalTextLength = this.text.length

    val textBeforeSelection = getTextBeforeSelection(maxChars = originalTextLength)
    val textAfterSelection = getTextAfterSelection(maxChars = originalTextLength)
    val selectedLineStartIndex = textBeforeSelection.indexOfLast { it == '\n' } + 1

    return this.copy(
        annotatedString = buildAnnotatedString {
            append(textBeforeSelection.slice(0..<selectedLineStartIndex))
            append(
                textBeforeSelection.slice(selectedLineStartIndex..textBeforeSelection.lastIndex).removePrefix(
                    " ".repeat(4)
                )
            )
            append(getSelectedText())
            append(textAfterSelection)
        },
        selection = TextRange(
            start = selection.start + 4,
            end = selection.end + 4
        )
    ).annotateAsMarkdown()
}

sealed class MarkdownEditorUiEvent {
    data class TextFieldValueChange(val textFieldValue: TextFieldValue) : MarkdownEditorUiEvent()
    data object ClickBold : MarkdownEditorUiEvent()
    data object ClickItalics : MarkdownEditorUiEvent()
    data object ClickIncreaseIndent : MarkdownEditorUiEvent()
    data object ClickDecreaseIndent : MarkdownEditorUiEvent()
    data object Save : MarkdownEditorUiEvent()
}

data class MarkdownEditorUiState(val textFieldValue: TextFieldValue = TextFieldValue()) {
    private val selectedSpanStyles = textFieldValue.getSelectedSpanStyles()
    val isBoldButtonSelected = selectedSpanStyles.any { it.item.fontWeight == FontWeight.Bold }
    val isItalicButtonSelected = selectedSpanStyles.any { it.item.fontStyle == FontStyle.Italic }
}

@Composable
fun MarkdownEditor(
    uiState: MarkdownEditorUiState,
    onEvent: (MarkdownEditorUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = MaterialTheme.shapes.medium

    Surface(
        shape = shape,
        tonalElevation = 4.dp,
        modifier = modifier
            .onFocusChanged { focusState ->
                if (!focusState.hasFocus) {
                    onEvent(MarkdownEditorUiEvent.Save)
                }
            }
    ) {
        Column(Modifier.padding(8.dp)) {
            MarkdownEditorButtonRow(
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(24.dp)
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
                    uiState = uiState,
                    onEvent = onEvent,
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
    uiState: MarkdownEditorUiState,
    onEvent: (MarkdownEditorUiEvent) -> Unit,
    modifier: Modifier
) {
    BasicTextField(
        value = uiState.textFieldValue,
        onValueChange = { onEvent(MarkdownEditorUiEvent.TextFieldValueChange(it)) },
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        modifier = modifier
    )
}

@Composable
private fun MarkdownEditorButtonRow(
    uiState: MarkdownEditorUiState,
    onEvent: (MarkdownEditorUiEvent) -> Unit,
    modifier: Modifier
) {
    Row(modifier = modifier) {
        MarkdownEditorStyleButton(
            onClick = { onEvent(MarkdownEditorUiEvent.ClickBold) },
            icon = Icons.Outlined.FormatBold,
            isSelected = uiState.isBoldButtonSelected
        )
        MarkdownEditorStyleButton(
            onClick = { onEvent(MarkdownEditorUiEvent.ClickItalics) },
            icon = Icons.Outlined.FormatItalic,
            isSelected = uiState.isItalicButtonSelected
        )
        VerticalDivider()
        MarkdownEditorStyleButton(
            onClick = { onEvent(MarkdownEditorUiEvent.ClickIncreaseIndent) },
            icon = Icons.Outlined.FormatIndentIncrease,
            isSelected = false
        )
        MarkdownEditorStyleButton(
            onClick = { onEvent(MarkdownEditorUiEvent.ClickDecreaseIndent) },
            icon = Icons.Outlined.FormatIndentDecrease,
            isSelected = false
        )
    }
}

@Composable
private fun MarkdownEditorStyleButton(
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
