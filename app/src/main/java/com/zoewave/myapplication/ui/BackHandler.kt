package com.zoewave.myapplication.ui

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.Composable
import androidx.compose.onCommit
import androidx.compose.remember
import androidx.compose.staticAmbientOf


private val AmbientBackPressedDispatcher = staticAmbientOf<OnBackPressedDispatcherOwner?> { null }

private class ComposableBackHandler(enabled: Boolean) : OnBackPressedCallback(enabled) {
    lateinit var onBackPressed: () -> Unit

    override fun handleOnBackPressed() {
        onBackPressed()
    }
}

@Composable
fun backButtonHandler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit
) {
    val dispatcher = (AmbientBackPressedDispatcher.current ?: return).onBackPressedDispatcher
    val handler = remember { ComposableBackHandler(enabled) }
    onCommit(dispatcher) {
        dispatcher.addCallback(handler)
        onDispose { handler.remove() }
    }
    onCommit(enabled) {
        handler.isEnabled = enabled
    }
    onCommit(onBackPressed) {
        handler.onBackPressed = onBackPressed
    }
}