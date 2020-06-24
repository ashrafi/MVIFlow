package com.zoewave.myapplication.model

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.zoewave.myapplication.room.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WordViewModel @ViewModelInject constructor(
    private val repository: WordRepo,
    private val stateChannel: StateChannel,
    @Assisted private val savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {

    val allWords: Flow<List<Word>> = repository.allWords

    // MVI channel and app state used by the views.
    val userIntentChannel = stateChannel.userIntentChannel
    val state = stateChannel.state

    init {
        // Setup State
        viewModelScope.launch {
            stateChannel.handleIntents()
        }
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Word?) = viewModelScope.launch(Dispatchers.IO) {
        word?.let {
            repository.insert(word)
        }
    }

    fun deleteAllWords() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllWords()
    }

    fun addAPIWord() = viewModelScope.launch(Dispatchers.IO) {
        repository.callAPI()
    }
}
