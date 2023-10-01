package com.mktwohy.needlenook.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import com.mktwohy.needlenook.viewmodels.FormulaScreenViewModel
import com.mktwohy.needlenook.viewmodels.ProjectScreenViewModel
import com.mktwohy.needlenook.ui.composables.formulascreen.FormulaScreen
import com.mktwohy.needlenook.ui.composables.projectscreen.ProjectScreen
import com.mktwohy.needlenook.util.extensions.noRippleClickable

@Composable
fun App(
    projectScreenViewModel: ProjectScreenViewModel,
    formulaScreenViewModel: FormulaScreenViewModel,
) {
    val focusManager = LocalFocusManager.current
    val navigationItemModels = listOf(
        NavigationItemModel(
            title = "Project",
            unselectedIcon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home
        ),
        NavigationItemModel(
            title = "Formulas",
            unselectedIcon = Icons.Outlined.Calculate,
            selectedIcon = Icons.Filled.Calculate
        )
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navigationItemModels.forEachIndexed { navIndex, navModel ->
                    val selected = navIndex == selectedTabIndex
                    NavigationBarItem(
                        selected = selected,
                        onClick = { selectedTabIndex = navIndex },
                        label = {
                            Text(navModel.title)
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) navModel.selectedIcon else navModel.unselectedIcon,
                                contentDescription = navModel.title
                            )
                        }
                    )
                }
            }
        }
    ) { scaffoldPadding ->
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .noRippleClickable { focusManager.clearFocus() }
        ) {
            Column {
                when (selectedTabIndex) {
                    0 -> ProjectScreen(
                        projectScreenUiState = projectScreenViewModel.uiState.collectAsState().value,
                        onProjectScreenEvent = { projectScreenViewModel.onUiEvent(it) },
                        markdownEditorUiState = projectScreenViewModel.markdownEditorUiState.collectAsState().value,
                        onMarkdownEditorEvent = { projectScreenViewModel.onUiEvent(it) },
                        modifier = Modifier.fillMaxSize()
                    )
                    1 -> FormulaScreen(formulaScreenViewModel, Modifier.fillMaxSize())
                    else -> error("Invalid tab/pager index: $selectedTabIndex")
                }
            }
        }
    }
}

data class NavigationItemModel(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)
