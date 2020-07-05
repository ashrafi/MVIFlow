package com.zoewave.myapplication.model

import com.zoewave.myapplication.di.ExampleApiService
import com.zoewave.myapplication.room.Word
import com.zoewave.myapplication.room.WordDao
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject


class WordRepo @Inject constructor(private val wordDao: WordDao, private val retrofit: Retrofit) {

    fun getAllWords() = wordDao.getAlphabetizedWords()

    private val callAPI = retrofit.create(ExampleApiService::class.java)

    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    suspend fun deleteAllWords() {
        wordDao.deleteAll()
    }

    // Retrofit Suspend Call
    suspend fun callAPI() {
        val exCall = callAPI.getExampleInfo().awaitResponse().body()
        wordDao.insert(Word("Title -> ${exCall?.title}"))
        wordDao.insert(Word("User ID -> ${exCall?.userId}"))
        wordDao.insert(Word("id -> ${exCall?.id}"))
    }

}

