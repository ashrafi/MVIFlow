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
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Delete
import androidx.ui.unit.dp
import androidx.ui.viewmodel.viewModel
import com.zoewave.myapplication.model.*
import com.zoewave.myapplication.room.Word
import javax.inject.Inject


class AddWordComposeUI @Inject constructor(
    private var vmAction: VMAction,
    private var viewState: ViewState
) {
    // Setup AmbientOf current state
    private val CurrAppState = ambientOf<ViewState> { viewState }


    @Composable
    fun ScaffoldWithBottomBar() {
        val wordViewModel = viewModel<WordViewModel>()
        val scaffoldState = remember { ScaffoldState() }
        // both bottom app bar and FAB need to know this shape
        val fabShape = RoundedCornerShape(50)
        val currState by wordViewModel.state.collectAsState(initial = viewState)
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
            if (CurrAppState.current.appState == AppState.Edit) {
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
            checked = (CurrAppState.current.appState == AppState.Edit),
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
        val currAppState = wordViewModel.state.collectAsState(initial = viewState)
        ConstraintLayout {

            val (filTxtField, addButton) = createRefs()

            Surface(color = Color.LightGray,
                modifier = Modifier.padding(16.dp)
                    .constrainAs(filTxtField) {
                        if (currAppState.value.appState == AppState.Edit)
                            bottom.linkTo(addButton.top)
                        else
                            top.linkTo(addButton.bottom)

                    }
                    .padding(5.dp)) {

                if (currAppState.value.appState == AppState.Edit) {
                    FilledTextField(
                        value = textValue,
                        onValueChange = { textValue = it },
                        label = { Text("Enter Your Text") },
                        placeholder = { Text(text = "") },
                        modifier = Modifier.padding(16.dp) + Modifier.fillMaxWidth()
                    )
                } else {
                    TextField(
                        value = textValue,
                        onValueChange = {},
                        modifier = Modifier.padding(16.dp) + Modifier.fillMaxWidth()
                    )
                }


            }
            Row(modifier = Modifier.constrainAs(addButton) {
                if (currAppState.value.appState == AppState.Edit)
                    top.linkTo(filTxtField.bottom)
                else
                    bottom.linkTo(filTxtField.top)
            }) {
                Button(
                    modifier = Modifier.padding(16.dp)
                        .drawBackground(Color.Cyan),
                    elevation = 5.dp,
                    //backgroundColor = if(canEdit) backgroundColor = Color.Gray,
                    onClick = {
                        // see if we can change the state.
                        if (currAppState.value.appState == AppState.Edit) {
                            vmAction.action(
                                MVOperation.InsertWord,
                                wordViewModel,
                                Word(textValue.text)
                            )
                            //vmAction.action(MVOperation.AddAPIWorld, wordViewModel)
                        }
                        // COMPOSE DOES NOT HAVE NAVIGATION FINALIZED!
                        // Workaround ---
                        // https://gist.github.com/adamp/62d13fe5bf0d6ddf9fcf58f8a6769523
                        navigateTo(
                            NavScreen.Home
                        )
                    }) {
                    if (currAppState.value.appState == AppState.Edit) {
                        Text(text = "Add Word", modifier = Modifier.padding(16.dp))
                    } else
                        Text(
                            text = "DISABLELED",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray
                        )
                }
                Button(
                    modifier = Modifier.padding(16.dp)
                        .drawBackground(Color.Cyan),
                    elevation = 5.dp,
                    //backgroundColor = if(canEdit) backgroundColor = Color.Gray,
                    onClick = {
                        // see if we can change the state.
                        if (currAppState.value.appState == AppState.Edit) {
                            vmAction.action(
                                MVOperation.AddAPIWorld,
                                wordViewModel
                            )
                            //vmAction.action(MVOperation.AddAPIWorld, wordViewModel)
                        }
                        // COMPOSE DOES NOT HAVE NAVIGATION FINALIZED!
                        // Workaround ---
                        // https://gist.github.com/adamp/62d13fe5bf0d6ddf9fcf58f8a6769523
                        navigateTo(
                            NavScreen.Home
                        )
                    }) {
                    if (currAppState.value.appState == AppState.Edit) {
                        Text(text = "Add API Words", modifier = Modifier.padding(16.dp))
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
}