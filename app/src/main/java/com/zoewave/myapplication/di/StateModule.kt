package com.zoewave.myapplication.di

import com.zoewave.myapplication.model.StateChannel
import com.zoewave.myapplication.model.ViewState
import com.zoewave.myapplication.model.WordRepo
import com.zoewave.myapplication.room.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Retrofit

// application scope
@InstallIn(ApplicationComponent::class)
@Module
object StateModule {

    /**
     * A mutable state flow is created using MutableStateFlow(value) constructor function with the
     * initial value. The value of mutable state flow can be updated by setting its value property.
     */
    @Provides
    fun providesChannel(wordRepo: WordRepo): StateChannel {
        return StateChannel(wordRepo)
    }

    @Provides
    fun providesMutableViewState(): MutableStateFlow<ViewState> {
        return MutableStateFlow(ViewState())
    }

    @Provides
    fun providesViewState(): ViewState {
        return ViewState()
    }

}