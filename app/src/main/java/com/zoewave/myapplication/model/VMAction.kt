package com.zoewave.myapplication.model

import com.zoewave.myapplication.room.Word
import javax.inject.Inject

sealed class MVOperation {
    // All and only MVI Actions
    object NotEdit : MVOperation()
    object CanEdit : MVOperation()
    object DeleteAll : MVOperation()
    object InsertWord : MVOperation()
    object AddAPIWorld : MVOperation()
    //object NewTest : MVOperation() // adding this will not compile
}


/**
 * When user interacts with the View, instances of Events(Actions) are generated and passed
 * on to the ViewModel.
 */
class VMAction @Inject constructor() {

    fun action(op: MVOperation, wordViewModel: WordViewModel, word: Word = Word("Hi")) =
        // "when" must be an operation for the compiler to check for state completeness
        when (op) {
            /* what MVVM to change data looks like
             MVOperation.InsertWord -> wordViewModel.insert(word = word)
             MVOperation.DeleteAll -> wordViewModel.deleteAllWords()
             MVOperation.AddAPIWorld -> wordViewModel.addAPIWord()*/

            /* The offer() method adds an element to the buffer immediately */
            /* send() is a suspendable operation, and it helps to synchronize the sender and the receiver. */

            // MVI to change the state of the app
            // These Events are modelled as a sealed class in the ViewModel
            MVOperation.NotEdit -> wordViewModel.userIntentChannel.offer(UserIntent.SetCanNotEdit)
            MVOperation.CanEdit -> wordViewModel.userIntentChannel.offer(UserIntent.SetCanEdit)
            MVOperation.AddAPIWorld -> wordViewModel.userIntentChannel.offer(UserIntent.UpdateAPIWords)
            MVOperation.DeleteAll -> wordViewModel.userIntentChannel.offer(UserIntent.DeleteWords)
            MVOperation.InsertWord -> wordViewModel.userIntentChannel.offer(
                UserIntent.UpdateWord(
                    word
                )
            )
        }

}

