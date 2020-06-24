package com.zoewave.myapplication.model

import androidx.annotation.WorkerThread
import com.zoewave.myapplication.api.ExampleAPI
import com.zoewave.myapplication.di.ExampleApiService
import com.zoewave.myapplication.room.Word
import com.zoewave.myapplication.room.WordDao
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.http.GET
import javax.inject.Inject


class WordRepo @Inject constructor(private val wordDao: WordDao, private val retrofit: Retrofit)  {

    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()
    val callAPI =  retrofit.create(ExampleApiService::class.java)

    // @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    @WorkerThread
    suspend fun deleteAllWords(){
        wordDao.deleteAll()
    }

    // Retrofit Suspend Call
    suspend fun callAPI() {
        //.getBikeAPInfo().awaitResponse().body().toString()
        val exCall = callAPI.getExampleInfo().awaitResponse().body()
        wordDao.insert(Word("Title -> ${exCall?.title}"))
        wordDao.insert(Word("User ID -> ${exCall?.userId}"))
        wordDao.insert(Word("id -> ${exCall?.id}"))
    }

}

