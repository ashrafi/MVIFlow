package com.zoewave.myapplication.ui

import androidx.compose.*
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextField
import androidx.ui.foundation.drawBackground
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.input.TextFieldValue
import androidx.ui.layout.Column
import androidx.ui.layout.ConstraintLayout
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Delete
import androidx.ui.unit.dp
import androidx.ui.viewmodel.viewModel
import com.zoewave.myapplication.model.*
import com.zoewave.myapplication.room.Word
import javax.inject.Inject


class AddWordComposeUI @Inject constructor(private var vmAction: VMAction) {
    // Setup AmbientOf current state
    private val CurrAppState = ambientOf<AppState> { AppState.Edit }


    @Composable
    fun ScaffoldWithBottomBar() {
        val wordViewModel = viewModel<WordViewModel>()
        val scaffoldState = remember { ScaffoldState() }
        // both bottom app bar and FAB need to know this shape
        val fabShape = RoundedCornerShape(50)
        val currState by wordViewModel.state.collectAsState(initial = AppState.NotEdit)
        CurrAppState.provides(currState)
        Providers(CurrAppState provides currState) {
            Scaffold(
                scaffoldState = scaffoldState,
                bodyContent = {
                    Column {
                        addWord(wordViewModel)
                    }
                },
                bottomBar = {
                    bottomBarAddWord(fabShape = fabShape)
                }
            )
        }
    }


    @Composable
    fun bottomBarAddWord(
        fabShape: RoundedCornerShape
    ) {
        val wordViewModel = viewModel<WordViewModel>()
        BottomAppBar(cutoutShape = fabShape) {
            if (CurrAppState.current == AppState.Edit) {
                IconButton(onClick = { vmAction.action(MVOperation.DeleteAll, wordViewModel) }) {
                    Icon(Icons.Filled.Delete)
                }
            }
            switchState(wordViewModel)
        }
    }


    @Composable
    fun switchState(wordViewModel: WordViewModel) {
        Switch(
            checked = (CurrAppState.current == AppState.Edit),
            onCheckedChange = {
                if (it) {
                    vmAction.action(MVOperation.CanEdit, wordViewModel)
                } else {
                    vmAction.action(MVOperation.NotEdit, wordViewModel)
                }
            },
            color = Color.Cyan
        )
    }

    @Composable
    fun addWord(wordViewModel: WordViewModel) {
        var textValue by state { TextFieldValue("") }
        val currAppState = wordViewModel.state.collectAsState(initial = AppState.Edit)
        ConstraintLayout {

            val (filTxtField, bot) = createRefs()

            Surface(color = Color.LightGray,
                modifier = Modifier.padding(16.dp)
                    .constrainAs(filTxtField) {
                        if (currAppState.value == AppState.Edit)
                            bottom.linkTo(bot.top)
                        else
                            top.linkTo(bot.bottom)

                    }
                    .padding(5.dp)) {

                if (currAppState.value == AppState.Edit) {
                    FilledTextField(
                        value = textValue,
                        onValueChange = { textValue = it },
                        label = { Text("Enter Your Text") },
                        placeholder = { Text(text = "") },
                        modifier = Modifier.padding(16.dp) + Modifier.fillMaxWidth()
                    )
                }else {
                    TextField(
                        value = textValue,
                        onValueChange = {},
                        modifier = Modifier.padding(16.dp) + Modifier.fillMaxWidth()
                    )
                }


            }
            Button(
                modifier = Modifier.padding(16.dp)
                    .constrainAs(bot) {
                        if (currAppState.value == AppState.Edit)
                            top.linkTo(filTxtField.bottom)
                        else
                            bottom.linkTo(filTxtField.top)
                    }
                    .drawBackground(Color.Cyan),
                elevation = 5.dp,
                //backgroundColor = if(canEdit) backgroundColor = Color.Gray,
                onClick = {
                    // see if we can change the state.
                    if (currAppState.value == AppState.Edit) {
                        wordViewModel.insert(Word(textValue.text))
                        vmAction.action(MVOperation.AddAPIWorld, wordViewModel)
                    }
                    // COMPOSE DOES NOT HAVE NAVIGATION FINALIZED!
                    // Workaround ---
                    // https://gist.github.com/adamp/62d13fe5bf0d6ddf9fcf58f8a6769523
                    navigateTo(NavScreen.Home)
                }) {
                if (currAppState.value == AppState.Edit) {
                    Text(text = "Add Word", modifier = Modifier.padding(16.dp))
                } else
                    Text(
                        text = "DISABLELED",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
            }

        }

    }
}