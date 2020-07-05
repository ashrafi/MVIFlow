package com.zoewave.myapplication.model

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WordViewModel @ViewModelInject constructor(
    private val repository: WordRepo,
    private val stateChannel: StateChannel,
    @Assisted private val savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {

    // MVI channel and app state used by the views.

    // Input from views
    val userIntentChannel = stateChannel.userIntentChannel

    // output to repo
    val state = stateChannel.state

    init {
        // Setup State
        viewModelScope.launch {
            stateChannel.handleIntents()
        }

    }
}
