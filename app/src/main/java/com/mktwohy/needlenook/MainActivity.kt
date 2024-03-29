package com.mktwohy.needlenook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.mktwohy.needlenook.ui.composables.App
import com.mktwohy.needlenook.ui.theme.NeedleNookTheme
import com.mktwohy.needlenook.viewmodels.FormulaScreenViewModel
import com.mktwohy.needlenook.viewmodels.ProjectScreenViewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val projectScreenViewModel: ProjectScreenViewModel by viewModels {
        ProjectScreenViewModel.Factory
    }
    private val formulaScreenViewModel: FormulaScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        setContent {
            NeedleNookTheme {
                App(projectScreenViewModel, formulaScreenViewModel)
            }
        }
    }
}
