package com.mktwohy.needlenook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mktwohy.needlenook.extensions.mutate
import com.mktwohy.needlenook.extensions.replaceIf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    suspend fun addProject(name: String) {
        dao.insertProject(Project(name))
    }

    suspend fun removeProject(project: Project) {
        check(project in _projects.value)

        dao.deleteProject(project)
    }

    fun selectProject(project: Project) {
        val projects = _projects.value
        _uiState.update { uiState ->
            check(project in projects)

            uiState.copy(selectedProjectIndex = projects.indexOf(project))
        }
    }

    fun renameSelectedProject(newName: String) {
        updateSelectedProject {
            it.copy(name = newName)
        }
    }

    fun incrementStitchCount() {
        updateSelectedProject { project ->
            project.copy(stitchCount = project.stitchCount + 1)
        }
    }

    fun decrementStitchCount() {
        updateSelectedProject { project ->
            project.copy(stitchCount = project.stitchCount - 1)
        }
    }

    fun resetStitchCount() {
        updateSelectedProject { project ->
            project.copy(stitchCount = 0)
        }
    }

    fun showResetDialog() {
        _uiState.update { uiState ->
            uiState.copy(showResetDialog = true)
        }
    }

    fun hideResetDialog() {
        _uiState.update { uiState ->
            uiState.copy(showResetDialog = true)
        }
    }

    private inline fun updateSelectedProject(
        crossinline transform: (Project) -> Project
    ) {
        _uiState.update { uiState ->
            uiState.copy(
                projects = uiState.projects.mutate {
                    replaceIf(
                        predicate = { it == uiState.selectedProject },
                        transform = transform
                    )
                }
            )
        }
    }
}

data class ProjectScreenUiState(
    val projects: List<Project> = emptyList(),
    val selectedProjectIndex: Int = 0,
    val showResetDialog: Boolean = false
) {
    val selectedProject: Project? = projects.getOrNull(selectedProjectIndex)
    val stitchCount: Int? = selectedProject?.stitchCount
    val decrementStitchCountIsEnabled: Boolean = stitchCount != null && stitchCount > 0
    val incrementStitchCountIsEnabled: Boolean = true
    val resetButtonIsEnabled: Boolean = stitchCount != null && stitchCount > 0
}