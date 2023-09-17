package com.mktwohy.needlenook.composables

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.mktwohy.needlenook.MainViewModel
import com.mktwohy.needlenook.ProjectScreenViewModel
import com.mktwohy.needlenook.Repository
import com.mktwohy.needlenook.extensions.noRippleClickable
import com.mktwohy.needlenook.ui.theme.NeedleNookTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App(projectScreenViewModel: ProjectScreenViewModel, viewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current
    val tabItems = listOf(
        TabModel(
            title = "Project",
            unselectedIcon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home
        ),
        TabModel(
            title = "Formulas",
            unselectedIcon = Icons.Outlined.Calculate,
            selectedIcon = Icons.Filled.Calculate
        )
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabItems.size }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickable { focusManager.clearFocus() }
    ) {
        Column {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabItems.forEachIndexed { tabIndex, tabModel ->
                    val selected = tabIndex == selectedTabIndex
                    Tab(
                        selected = selected,
                        onClick = { selectedTabIndex = tabIndex },
                        text = { Text(tabModel.title) },
                        icon = {
                            Icon(
                                imageVector = if (selected) tabModel.selectedIcon else tabModel.unselectedIcon,
                                contentDescription = tabModel.title
                            )
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                when (index) {
                    0 -> ProjectScreen(
                        uiState = projectScreenViewModel.uiState.collectAsState().value,
                        onEvent = projectScreenViewModel::onEvent,
                        modifier = Modifier.fillMaxSize()
                    )
                    1 -> FormulaScreen(viewModel, Modifier.fillMaxSize())
                    else -> error("Invalid tab/pager index: $index")
                }
            }
        }
    }
}

//@Preview(name = "Light Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun Preview() {
//    NeedleNookTheme {
//        App(MainViewModel(repository = Repository(LocalContext.current)))
//    }
//}

data class TabModel(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)
