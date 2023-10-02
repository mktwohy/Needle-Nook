package com.mktwohy.needlenook.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(
    val route: String,
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {
    companion object {
        val screens = listOf(Project, Formulas)
    }

    data object Project : Screen(
        route = "project_screen",
        title = "Project",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )
    data object Formulas : Screen(
        route = "formulas_screen",
        title = "Formulas",
        unselectedIcon = Icons.Outlined.Calculate,
        selectedIcon = Icons.Filled.Calculate
    )
}