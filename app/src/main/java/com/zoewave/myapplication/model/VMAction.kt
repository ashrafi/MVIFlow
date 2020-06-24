package com.zoewave.myapplication.model

import android.util.Log
import com.zoewave.myapplication.room.Word
import javax.inject.Inject

enum class MVOperation {
    DeleteAll, NotEdit, CanEdit, InsertWord, AddAPIWorld;
}

class VMAction @Inject constructor() {

    fun action(op: MVOperation, wordViewModel: WordViewModel, word: Word? = null) {
        Log.v("MVI", "Operation got called ${op}")
        when (op) {

            // MVVM to change data
            MVOperation.InsertWord -> wordViewModel.insert(word = word)
            MVOperation.DeleteAll -> wordViewModel.deleteAllWords()
            MVOperation.AddAPIWorld -> wordViewModel.addAPIWord()

            // MVI to change the state of the app
            MVOperation.NotEdit -> wordViewModel.userIntentChannel.offer(UserIntent.SetCanNotEdit)
            MVOperation.CanEdit -> wordViewModel.userIntentChannel.offer(UserIntent.SetCanEdit)
        }

    }

}