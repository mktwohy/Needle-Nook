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
import com.mktwohy.needlenook.ui.composables.projectscreen.MarkdownEditorUiEvent
import com.mktwohy.needlenook.ui.composables.projectscreen.MarkdownEditorUiState
import com.mktwohy.needlenook.ui.composables.projectscreen.annotateAsMarkdown
import com.mktwohy.needlenook.ui.composables.projectscreen.applyMdStyleToSelection
import com.mktwohy.needlenook.ui.composables.projectscreen.decreaseSelectedLineIndent
import com.mktwohy.needlenook.ui.composables.projectscreen.increaseSelectedLineIndent
import com.mktwohy.needlenook.ui.composables.projectscreen.removeMdStyleFromSelection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
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

    init {
        viewModelScope.launch {
            val initialProjectList = _projects.first { it.isNotEmpty() }[0]
            onUiEvent(ProjectScreenUiEvent.SelectProject(initialProjectList))
        }
    }

    private val _markdownEditorUiState = MutableStateFlow(MarkdownEditorUiState())
    val markdownEditorUiState = _markdownEditorUiState.asStateFlow()

    private val _uiState = MutableStateFlow(ProjectScreenUiState())
    val uiState = _uiState
        .combine(_projects) { uiState, projects ->
            uiState.copy(projects = projects)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = ProjectScreenUiState()
        )

    fun onUiEvent(event: MarkdownEditorUiEvent) {
        Timber.d("Project Screen UI Event: $event")
        when (event) {
            is MarkdownEditorUiEvent.TextFieldValueChange -> {
                updateTextFieldValue {
                    event.textFieldValue.annotateAsMarkdown()
                }
            }
            is MarkdownEditorUiEvent.ClickBold -> {
                updateTextFieldValue {
                    if (isBoldButtonSelected) {
                        it.removeMdStyleFromSelection("**")
                    } else {
                        it.applyMdStyleToSelection("**")
                    }
                }
            }
            is MarkdownEditorUiEvent.ClickItalics -> {
                updateTextFieldValue {
                    if (isItalicButtonSelected) {
                        it.removeMdStyleFromSelection("*")
                    } else {
                        it.applyMdStyleToSelection("*")
                    }
                }
            }
            is MarkdownEditorUiEvent.ClickIncreaseIndent -> {
                updateTextFieldValue { it.increaseSelectedLineIndent() }
            }
            is MarkdownEditorUiEvent.ClickDecreaseIndent -> {
                updateTextFieldValue {
                    it.decreaseSelectedLineIndent()
                }
            }
            is MarkdownEditorUiEvent.Save -> {
                viewModelScope.launch {
                    val selectedProject = uiState.value.selectedProject
                    val notes = markdownEditorUiState.value.textFieldValue.text
                    if (selectedProject != null) {
                        dao.updateProject(selectedProject.copy(notes = notes))
                    }
                }
            }
        }
    }

    private fun updateTextFieldValue(transform: MarkdownEditorUiState.(TextFieldValue) -> TextFieldValue) {
        viewModelScope.launch {
            _markdownEditorUiState.update { uiState ->
                uiState.copy(textFieldValue = uiState.transform(uiState.textFieldValue))
            }
        }
    }

    fun onUiEvent(event: ProjectScreenUiEvent) {
        when (event) {
            is ProjectScreenUiEvent.AddProject -> {
                val project = Project(event.name)
                onUiEvent(ProjectScreenUiEvent.HideDialog)
                viewModelScope.launch {
                    // Wait for UiState to be updated with project
                    uiState.filter { project in it.projects }.first()
                    onUiEvent(ProjectScreenUiEvent.SelectProject(project))
                }
                viewModelScope.launch(Dispatchers.IO) {
                    dao.insertProject(Project(event.name))
                }
            }
            is ProjectScreenUiEvent.RemoveProject -> {
                onUiEvent(ProjectScreenUiEvent.HideDialog)

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
                updateTextFieldValue {
                    TextFieldValue(event.project.notes).annotateAsMarkdown()
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
                    onUiEvent(ProjectScreenUiEvent.HideDialog)
                }
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
    data class ShowDialog(val dialog: ProjectScreenDialog) : ProjectScreenUiEvent
    data object HideDialog : ProjectScreenUiEvent
}