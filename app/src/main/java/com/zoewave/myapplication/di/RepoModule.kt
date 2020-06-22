package com.zoewave.myapplication.di

import com.zoewave.myapplication.room.WordDao
import com.zoewave.myapplication.model.WordRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
object RepoModule {

    @Provides
    @ActivityRetainedScoped
    fun provideWordRepo(wordDao: WordDao): WordRepo {
        return WordRepo(wordDao)
    }
}
