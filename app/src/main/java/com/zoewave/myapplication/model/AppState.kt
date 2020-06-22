package com.zoewave.myapplication.model

import androidx.compose.getValue
import androidx.compose.mutableStateOf
import androidx.compose.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

//the intents are handled in a coroutine actor (viewModelScope.launch)
sealed class AppState {
    object Edit : AppState()
    object NotEdit : AppState()
}

sealed class UserIntent {
    object SetCanEdit : UserIntent()
    object SetCanNotEdit : UserIntent()
}

/**
 * https://gitlab.com/dan-0/simplemvi/-/blob/as_presentation/app/src/main/java/me/danlowe/simplemvi/MviViewModel.kt
 */
class StateChannel @Inject constructor(private val _state : MutableStateFlow<AppState>) {

    // basic Channel<T> to listen to intents and state changes in the ViewModel
    val userIntentChannel = Channel<UserIntent>()

    // The StateFlow API was designed specifically to manage a state.
    // Move to SharedFlow https://github.com/Kotlin/kotlinx.coroutines/issues/2034

    // SharedFlow  Flow.shareIn and stateIn operators
    // https://github.com/Kotlin/kotlinx.coroutines/issues/2047

    val state: StateFlow<AppState>
        get() = _state

    suspend fun handleIntents() {
        userIntentChannel.consumeAsFlow().collect() { userIntent ->
            when (userIntent) {
                UserIntent.SetCanEdit -> {
                    _state.value = AppState.Edit
                }
                UserIntent.SetCanNotEdit -> {
                    _state.value = AppState.NotEdit
                }
            }
        }
    }
}


// Navigation
/**
 * Class defining the screens we have in the app: home, article details and interests
 */
sealed class NavScreen {
    object Home : NavScreen()
    object AddWord : NavScreen()
}

object AppScreen {
    var currentScreen by mutableStateOf<NavScreen>(NavScreen.Home)
}

/**
 * Temporary solution pending navigation support.
 */
fun navigateTo(destination: NavScreen) {
    AppScreen.currentScreen = destination
}

