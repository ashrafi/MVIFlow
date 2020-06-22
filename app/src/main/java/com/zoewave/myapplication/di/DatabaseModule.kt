package com.zoewave.myapplication.di

import android.content.Context
import androidx.room.Room
import com.zoewave.myapplication.model.WordViewModel
import com.zoewave.myapplication.room.WordDao
import com.zoewave.myapplication.room.WordRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

// application scope
@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): WordRoomDatabase {
        return Room.databaseBuilder(
            appContext,
            WordRoomDatabase::class.java,
            "word.db"
        ).build()
    }

    @Provides
    fun provideWordDao(database: WordRoomDatabase): WordDao {
        return database.wordDao()
    }

}