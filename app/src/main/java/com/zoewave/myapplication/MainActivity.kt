package com.zoewave.myapplication

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.*
import androidx.ui.animation.Crossfade
import androidx.ui.core.setContent
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
//import androidx.ui.livedata.observeAsState
import com.zoewave.myapplication.model.*
import com.zoewave.myapplication.ui.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // inject viewModel
    private val wordViewModel: WordViewModel by viewModels()

    @Inject
    lateinit var vmAction : VMAction

    @Inject
    lateinit var addWordComposeUI : AddWordComposeUI

    @Inject
    lateinit var mainComposeUI: MainComposeUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme() {
                AppContent(mainComposeUI, addWordComposeUI, vmAction, wordViewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

@Composable
private fun AppContent(
    mainComposeUI: MainComposeUI,
    addWordComposeUI: AddWordComposeUI,
    vmAction: VMAction,
    wordViewModel: WordViewModel
) {
    Crossfade(AppScreen.currentScreen) { screen ->
        Surface(color = MaterialTheme.colors.background) {

            backButtonHandler(onBackPressed = { navigateTo(NavScreen.Home) })
            when (screen) {
                is NavScreen.Home -> mainComposeUI.HomeScreen(wordViewModel)
                is NavScreen.AddWord -> addWordComposeUI.ScaffoldWithBottomBar(wordViewModel = wordViewModel)
            }
        }
    }
}