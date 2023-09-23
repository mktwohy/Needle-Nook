package com.mktwohy.needlenook.viewmodels

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mktwohy.needlenook.NeedleNookApplication
import com.mktwohy.needlenook.data.Project
import com.mktwohy.needlenook.data.ProjectDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

class ProjectScreenViewModel(private val dao: ProjectDao) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as NeedleNookApplication
                ProjectScreenViewModel(app.database.projectDao)
            }
        }
    }

    private val _projects = dao
        .getProjectsOrderedByName()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(ProjectScreenUiState())
    val uiState = _uiState
        .combine(_projects) { uiState, projects ->
            uiState.copy(projects = projects)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = ProjectScreenUiState()
        )

    fun onEvent(event: ProjectScreenUiEvent) {
        when (event) {
            is ProjectScreenUiEvent.AddProject -> {
                val project = Project(event.name)
                onEvent(ProjectScreenUiEvent.HideDialog)
                viewModelScope.launch {
                    // Wait for UiState to be updated with project
                    uiState.filter { project in it.projects }.first()
                    onEvent(ProjectScreenUiEvent.SelectProject(project))
                }
                viewModelScope.launch(Dispatchers.IO) {
                    dao.insertProject(Project(event.name))
                }
            }
            is ProjectScreenUiEvent.RemoveProject -> {
                onEvent(ProjectScreenUiEvent.HideDialog)

                viewModelScope.launch(Dispatchers.IO) {
                    dao.deleteProject(event.project)
                }
            }
            is ProjectScreenUiEvent.SelectProject -> {
                val projects = _projects.value
                _uiState.update { uiState ->
                    check(event.project in projects)

                    uiState.copy(selectedProjectIndex = projects.indexOf(event.project))
                }
            }
            is ProjectScreenUiEvent.RenameSelectedProject -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val selectedProject = uiState.value.selectedProject
                    checkNotNull(selectedProject)
                    dao.updateProject(selectedProject.copy(name = event.name))
                }
            }
            is ProjectScreenUiEvent.IncrementStitchCounter -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val selectedProject = uiState.value.selectedProject
                    checkNotNull(selectedProject)
                    dao.updateProject(selectedProject.copy(stitchCount = selectedProject.stitchCount + 1))
                }
            }
            is ProjectScreenUiEvent.DecrementStitchCounter -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val selectedProject = uiState.value.selectedProject
                    checkNotNull(selectedProject)
                    dao.updateProject(selectedProject.copy(stitchCount = selectedProject.stitchCount - 1))
                }
            }
            is ProjectScreenUiEvent.ResetStitchCounter -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val selectedProject = uiState.value.selectedProject
                    checkNotNull(selectedProject)
                    dao.updateProject(selectedProject.copy(stitchCount = 0))
                    onEvent(ProjectScreenUiEvent.HideDialog)
                }
            }
            is ProjectScreenUiEvent.EditNotes -> {
                _uiState.update { uiState ->
                    uiState.copy(notes = event.value)
                }
            }
            is ProjectScreenUiEvent.SaveNotes -> {
                TODO()
            }
            is ProjectScreenUiEvent.ShowDialog -> {
                _uiState.update { uiState ->
                    uiState.copy(dialog = event.dialog)
                }
            }
            is ProjectScreenUiEvent.HideDialog -> {
                Timber.d("Hide dialog")
                _uiState.update { uiState ->
                    uiState.copy(dialog = null)
                }
            }
        }
    }
}

data class ProjectScreenUiState(
    val projects: List<Project> = emptyList(),
    val selectedProjectIndex: Int = 0,
    val notes: TextFieldValue = TextFieldValue(
        """
            ## Header 2
            Plain Text
            - *Italics*
            - **Bold**
              1. ***Bold and Italics***
              2. `code`
        """.trimIndent(),
    ),
    val dialog: ProjectScreenDialog? = null
) {
    val selectedProject: Project? = projects.getOrNull(selectedProjectIndex)
    val stitchCount: Int? = selectedProject?.stitchCount
    val decrementStitchCountIsEnabled: Boolean = stitchCount != null && stitchCount > 0
    val incrementStitchCountIsEnabled: Boolean = true
    val resetButtonIsEnabled: Boolean = stitchCount != null && stitchCount > 0
}

sealed interface ProjectScreenDialog {
    data object ResetDialog : ProjectScreenDialog
    data object AddProject : ProjectScreenDialog
    data class RemoveProject(val project: Project) : ProjectScreenDialog
}

sealed interface ProjectScreenUiEvent {
    data class AddProject(val name: String) : ProjectScreenUiEvent
    data class RemoveProject(val project: Project) : ProjectScreenUiEvent
    data class SelectProject(val project: Project) : ProjectScreenUiEvent
    data class RenameSelectedProject(val name: String) : ProjectScreenUiEvent
    data object IncrementStitchCounter : ProjectScreenUiEvent
    data object DecrementStitchCounter : ProjectScreenUiEvent
    data object ResetStitchCounter : ProjectScreenUiEvent
    data class EditNotes(val value: TextFieldValue) : ProjectScreenUiEvent
    data object SaveNotes : ProjectScreenUiEvent
    data class ShowDialog(val dialog: ProjectScreenDialog) : ProjectScreenUiEvent
    data object HideDialog : ProjectScreenUiEvent
}