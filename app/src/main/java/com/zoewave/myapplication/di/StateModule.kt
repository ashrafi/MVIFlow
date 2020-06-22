package com.zoewave.myapplication.di

import com.zoewave.myapplication.model.AppState
import com.zoewave.myapplication.model.StateChannel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

// application scope
@InstallIn(ApplicationComponent::class)
@Module
object StateModule {

    /**
     * A mutable state flow is created using MutableStateFlow(value) constructor function with the
     * initial value. The value of mutable state flow can be updated by setting its value property.
     */
    @Provides
    fun providesChannel(state: MutableStateFlow<AppState>) : StateChannel {
        return StateChannel(state)
    }

    @Provides
    @Singleton
    fun providesAppState() : MutableStateFlow<AppState> {
        return MutableStateFlow<AppState>(AppState.Edit)
    }

}