package com.zoewave.myapplication.di

import com.zoewave.myapplication.model.WordRepo
import com.zoewave.myapplication.room.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@InstallIn(ActivityRetainedComponent::class)
@Module
object RepoModule {

    @Provides
    @ActivityRetainedScoped
    fun provideWordRepo(wordDao: WordDao, retrofit: Retrofit): WordRepo {
        return WordRepo(wordDao, retrofit)
    }
}
