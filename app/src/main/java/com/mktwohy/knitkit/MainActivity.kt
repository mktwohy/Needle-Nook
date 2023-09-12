package com.mktwohy.knitkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.mktwohy.knitkit.composables.App
import com.mktwohy.knitkit.ui.theme.KnitKitTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        setContent {
            KnitKitTheme { App(viewModel) }
        }
    }

    override fun onPause() {
        viewModel.saveState()
        super.onPause()
    }
}
