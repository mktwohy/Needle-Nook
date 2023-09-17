package com.mktwohy.needlenook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mktwohy.needlenook.composables.App
import com.mktwohy.needlenook.ui.theme.NeedleNookTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val projectScreenViewModel: ProjectScreenViewModel by viewModels { ProjectScreenViewModel.Factory }
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        setContent {
            NeedleNookTheme { App(projectScreenViewModel, viewModel) }
        }
    }

    override fun onPause() {
        viewModel.saveState()
        super.onPause()
    }
}
