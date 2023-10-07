package com.mktwohy.needlenook.ui.composables.projectscreen

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mktwohy.needlenook.ui.composables.AlertDialog
import com.mktwohy.needlenook.ui.theme.NeedleNookTheme
import com.mktwohy.needlenook.viewmodels.ProjectScreenDialog
import com.mktwohy.needlenook.viewmodels.ProjectScreenUiEvent
import com.mktwohy.needlenook.viewmodels.ProjectScreenUiState
import kotlinx.coroutines.android.awaitFrame

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProjectScreen(
    projectScreenUiState: ProjectScreenUiState,
    onProjectScreenEvent: (ProjectScreenUiEvent) -> Unit,
    markdownEditorUiState: MarkdownEditorUiState,
    onMarkdownEditorEvent: (MarkdownEditorUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onProjectScreenEvent(
                        ProjectScreenUiEvent.ShowDialog(ProjectScreenDialog.AddProject)
                    )
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(modifier.padding(paddingValues)) {
            ProjectDropdownMenu(
                uiState = projectScreenUiState,
                onEvent = onProjectScreenEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )

            val selectedProject = projectScreenUiState.selectedProject
            if (selectedProject != null) {
                StitchCounter(
                    count = selectedProject.stitchCount,
                    onIncrement = {
                        onProjectScreenEvent(ProjectScreenUiEvent.IncrementStitchCounter)
                    },
                    onDecrement = {
                        onProjectScreenEvent(ProjectScreenUiEvent.DecrementStitchCounter)
                    },
                    onReset = {
                        val dialog = ProjectScreenDialog.ResetDialog
                        onProjectScreenEvent(ProjectScreenUiEvent.ShowDialog(dialog))
                    },
                    isIncrementEnabled = projectScreenUiState.incrementStitchCountIsEnabled,
                    isDecrementEnabled = projectScreenUiState.decrementStitchCountIsEnabled,
                    isResetEnabled = projectScreenUiState.resetButtonIsEnabled,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                )
                MarkdownEditor(
                    uiState = markdownEditorUiState,
                    onEvent = onMarkdownEditorEvent,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }
    }
    when (projectScreenUiState.dialog) {
        is ProjectScreenDialog.ResetDialog -> {
            AlertDialog(
                title = "Confirm Reset",
                message = "Are you sure you want to reset? Count will be lost.",
                confirm = "OK",
                dismiss = "Cancel",
                onConfirm = { onProjectScreenEvent(ProjectScreenUiEvent.ResetStitchCounter) },
                onDismiss = { onProjectScreenEvent(ProjectScreenUiEvent.HideDialog) }
            )
        }
        is ProjectScreenDialog.RemoveProject -> {
            val project = projectScreenUiState.dialog.project
            AlertDialog(
                title = "Confirm Remove",
                message = "Are you sure you want to remove \"${project.name}\"?",
                confirm = "Remove",
                dismiss = "Cancel",
                onConfirm = { onProjectScreenEvent(ProjectScreenUiEvent.RemoveProject(project)) },
                onDismiss = { onProjectScreenEvent(ProjectScreenUiEvent.HideDialog) }
            )
        }
        is ProjectScreenDialog.AddProject -> {
            var name by remember { mutableStateOf("") }
            val focusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current

            AlertDialog(
                title = "Add Project",
                content = {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Project Name") },
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                },
                confirm = "Add",
                dismiss = "Cancel",
                onConfirm = { onProjectScreenEvent(ProjectScreenUiEvent.AddProject(name)) },
                onDismiss = { onProjectScreenEvent(ProjectScreenUiEvent.HideDialog) }
            )

            LaunchedEffect(focusRequester) {
                awaitFrame()
                keyboardController?.show()
                focusRequester.requestFocus()
            }
        }
        null -> {
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDropdownMenu(
    uiState: ProjectScreenUiState,
    onEvent: (ProjectScreenUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = uiState.selectedProject?.name ?: "",
            onValueChange = { },
            label = { Text("Selected Project") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                errorContainerColor = containerColor,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            for (project in uiState.projects) {
                DropdownMenuItem(
                    text = { Text(text = project.name) },
                    trailingIcon = {
                        IconButton(onClick = {
                            val dialog = ProjectScreenDialog.RemoveProject(project)
                            onEvent(ProjectScreenUiEvent.ShowDialog(dialog))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove Project"
                            )
                        }
                    },
                    onClick = {
                        isExpanded = false
                        onEvent(ProjectScreenUiEvent.SelectProject(project))
                    }
                )
            }
        }
    }
}

@Preview(name = "Light Theme", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark Theme", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    NeedleNookTheme {
        ProjectScreen(
            projectScreenUiState = ProjectScreenUiState(),
            onProjectScreenEvent = { },
            markdownEditorUiState = MarkdownEditorUiState(),
            onMarkdownEditorEvent = { },
            modifier = Modifier.fillMaxSize()
        )
    }
}
