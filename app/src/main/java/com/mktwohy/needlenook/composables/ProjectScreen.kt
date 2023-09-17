package com.mktwohy.needlenook.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mktwohy.needlenook.MainViewModel
import com.mktwohy.needlenook.ProjectDao
import com.mktwohy.needlenook.ProjectScreenViewModel
import com.mktwohy.needlenook.Repository
import com.mktwohy.needlenook.ui.theme.NeedleNookTheme

@Composable
fun ProjectScreen(viewModel: ProjectScreenViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier) {
        val project = uiState.selectedProject
        if (project != null) {
            StitchCounter(
                count = project.stitchCount,
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