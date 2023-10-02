package com.mktwohy.needlenook.ui.composables

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
    val navController = rememberNavController()
    val focusManager = LocalFocusManager.current

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                for (screen in Screen.screens) {
                    val selected = currentDestination
                        ?.hierarchy
                        ?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to avoid building up
                                // a large stack of destinations on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true

                            }
                        },
                        label = { Text(screen.title) },
                        icon = {
                            Icon(
                                imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.title
                            )
                        }
                    )
                }
            }
        }
    ) { scaffoldPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Project.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .noRippleClickable { focusManager.clearFocus() }
        ) {
            val slideAnimationSpec = tween<IntOffset>(durationMillis = 200)
            val fadeAnimationSpec = tween<Float>(durationMillis = 100)

            composable(
                route = Screen.Project.route,
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = slideAnimationSpec,
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                },
                exitTransition = { fadeOut(fadeAnimationSpec) }
            ) {
                ProjectScreen(
                    projectScreenUiState = projectScreenViewModel.uiState.collectAsState().value,
                    onProjectScreenEvent = { projectScreenViewModel.onUiEvent(it) },
                    markdownEditorUiState = projectScreenViewModel.markdownEditorUiState.collectAsState().value,
                    onMarkdownEditorEvent = { projectScreenViewModel.onUiEvent(it) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(
                route = Screen.Formulas.route,
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = slideAnimationSpec,
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(fadeAnimationSpec)
                }
            ) {
                FormulaScreen(formulaScreenViewModel, Modifier.fillMaxSize())
            }
        }
    }
}
