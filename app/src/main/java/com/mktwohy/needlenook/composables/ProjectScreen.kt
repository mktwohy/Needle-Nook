package com.mktwohy.needlenook.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mktwohy.needlenook.Project
import com.mktwohy.needlenook.ProjectScreenUiState
import com.mktwohy.needlenook.ProjectScreenViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ProjectScreen(viewModel: ProjectScreenViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        viewModel.addProject("My Project ${uiState.projects.lastIndex + 1}")
                    }
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
                uiState = uiState,
                onSelectedProjectNameChange = {  },
                onSelectProject = viewModel::selectProject
            )

            val selectedProject = uiState.selectedProject
            if (selectedProject != null) {
                StitchCounter(
                    count = selectedProject.stitchCount,
                    onIncrement = viewModel::incrementStitchCount,
                    onDecrement = viewModel::decrementStitchCount,
                    onReset = viewModel::showResetDialog,
                    isIncrementEnabled = uiState.incrementStitchCountIsEnabled,
                    isDecrementEnabled = uiState.decrementStitchCountIsEnabled,
                    isResetEnabled = uiState.resetButtonIsEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
    AlertDialog(
        show = uiState.showResetDialog,
        title = "Confirm Reset",
        message = "Are you sure you want to reset? Count will be lost.",
        confirm = "OK",
        dismiss = "Cancel",
        onConfirm = viewModel::resetStitchCount,
        onDismiss = viewModel::hideResetDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDropdownMenu(
    uiState: ProjectScreenUiState,
    onSelectedProjectNameChange: (String) -> Unit,
    onSelectProject: (Project) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it }
    ) {
        TextField(
            readOnly = true,
            value = uiState.selectedProject?.name ?: "",
            onValueChange = onSelectedProjectNameChange,
            label = { Text("Selected Project") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            for (project in uiState.projects) {
                DropdownMenuItem(
                    text = { Text(text = project.name) },
                    onClick = {
                        isExpanded = false
                        onSelectProject(project)
                    }
                )
            }
        }
    }
}

//@Preview(name = "Light Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun Preview() {
//    NeedleNookTheme {
//        ProjectScreen(
//            viewModel = ProjectScreenViewModel,
//            modifier = Modifier
//                .fillMaxSize()
//        )
//    }
//}