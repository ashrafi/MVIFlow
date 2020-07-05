package com.zoewave.myapplication.model

import com.zoewave.myapplication.room.Word
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

//the intents are handled in a coroutine actor (viewModelScope.launch)
sealed class AppState {
    object Edit : AppState()
    object NotEdit : AppState()
}

data class ViewState(
    val progress: Boolean = false,
    val error: Boolean = false,
    var appState: AppState = AppState.Edit,
    val allWords: Flow<List<Word>> = flowOf(ArrayList<Word>().toList())
)

/**
 * When the user interacts with the View, instances of Events are generated and
 * passed on to the ViewModel.  These Events are modelled as a sealed class.
 */
sealed class UserIntent {
    object SetCanEdit : UserIntent()
    object SetCanNotEdit : UserIntent()
    object DeleteWords : UserIntent()
    object UpdateAPIWords : UserIntent()
    data class UpdateWord(val word: Word) : UserIntent()
    //object NewTest : UserIntent()  // will not compile and catch logic error
}


/**
 * The ViewModel acts upon these events accordingly by making API calls or saving/retrieving data in the database via the Repository layer.
 */
class StateChannel @Inject constructor(private val repository: WordRepo) {//private val _state : MutableStateFlow<ViewState>) {

    // basic Channel<T> to listen to intents and state changes in the ViewModel
    val userIntentChannel = Channel<UserIntent>()

    private val _state = MutableStateFlow(ViewState())


    val state: StateFlow<ViewState>
        get() = _state

    suspend fun handleIntents() {
        // ViewModel update the repository layer.
        // Use the Flow to consume the Channel values. -- ChannelFlow
        // https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/channel-flow.html


        userIntentChannel.consumeAsFlow().collect { userIntent ->
            // create a new viewState and set that to the value in the channel
            // Render of the MVI
            //TODO: add LCE<Result>(Loading/Content/Error)
            _state.value = render(userIntent)

        }
    }

    /**
     * Takes old state and creates a new immutable state for the UI to render.
     */
    private suspend fun render(userIntent: UserIntent) = when (userIntent) {

        // Update state of app -- Not / Edit
        UserIntent.SetCanEdit -> {
            _state.value.copy(appState = AppState.Edit)
        }

        UserIntent.SetCanNotEdit -> {
            _state.value.copy(appState = AppState.NotEdit)
        }

        // Update data - list of words
        UserIntent.UpdateAPIWords -> {
            repository.callAPI()
            _state.value.copy(allWords = repository.getAllWords())
        }

        // "is" only needed for classes not objects
        is UserIntent.UpdateWord -> {
            repository.insert(userIntent.word)
            _state.value.copy(allWords = repository.getAllWords())
        }

        UserIntent.DeleteWords -> {
            repository.deleteAllWords()
            _state.value.copy(allWords = repository.getAllWords())
        }
    }
}