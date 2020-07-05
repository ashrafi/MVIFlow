package com.zoewave.myapplication.ui

import androidx.compose.getValue
import androidx.compose.mutableStateOf
import androidx.compose.setValue

// Navigation
/**
 * Class defining the screens we have in the app: home, article details and interests
 */
sealed class NavScreen {
    object Home : NavScreen()
    object AddWord : NavScreen()
}

object AppScreen {
    var currentScreen by mutableStateOf<NavScreen>(
        NavScreen.Home
    )
}

/**
 * Temporary solution pending navigation support.
 */
fun navigateTo(destination: NavScreen) {
    AppScreen.currentScreen = destination
}

