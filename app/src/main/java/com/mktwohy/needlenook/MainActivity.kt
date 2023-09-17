package com.mktwohy.needlenook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.mktwohy.needlenook.composables.App
import com.mktwohy.needlenook.ui.theme.NeedleNookTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val repository: Repository by application { repository }
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        setContent {
            NeedleNookTheme { App(viewModel) }
        }
    }

    override fun onPause() {
        viewModel.saveState()
        super.onPause()
    }
}
