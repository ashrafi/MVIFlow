package com.zoewave.myapplication.model

import androidx.annotation.WorkerThread
import com.zoewave.myapplication.room.Word
import com.zoewave.myapplication.room.WordDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WordRepo @Inject constructor(private val wordDao: WordDao)  {

    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    @WorkerThread
    suspend fun deleteAllWords(){
        wordDao.deleteAll()
    }

    // Retrofit Suspend Call
}