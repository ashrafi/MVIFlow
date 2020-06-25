package com.zoewave.myapplication.ui

import androidx.compose.*
import androidx.ui.core.Modifier
import androidx.ui.core.tag
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.drawBackground
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.input.TextFieldValue
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Delete
import androidx.ui.material.icons.filled.Face
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
                        //DemoInlineDSL()
                        DemoConstraintSet()
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
                IconButton(
                    modifier = Modifier.drawBackground(color = Color.Gray),
                    onClick = {
                        vmAction.action(MVOperation.AddAPIWorld, wordViewModel)
                    }) {
                    Icon(Icons.Filled.Face)
                }
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
        Column() {
            var textValue by state { TextFieldValue("") }
            val currAppState = wordViewModel.state.collectAsState(initial = AppState.Edit)
            Column() {
                Surface(color = Color.LightGray, modifier = Modifier.padding(16.dp)) {

                    FilledTextField(
                        value = textValue,
                        onValueChange = { textValue = it },
                        label = { Text("Enter Your Text") },
                        placeholder = { Text(text = "") },
                        modifier = Modifier.padding(16.dp) + Modifier.fillMaxWidth()
                    )

                }

                Button(
                    modifier = Modifier.padding(16.dp),
                    elevation = 5.dp,
                    //backgroundColor = if(canEdit) backgroundColor = Color.Gray,
                    onClick = {
                        // see if we can change the state.
                        if (currAppState.value == AppState.Edit) {
                            wordViewModel.insert(Word(textValue.text))
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

    @Composable
    fun DemoInlineDSL() {
        ConstraintLayout {
            val (text1, text2, text3) = createRefs()

            Text("Text1", Modifier.constrainAs(text1) {
                start.linkTo(text2.end, margin = 20.dp)
            })
            Text("Text2", Modifier.constrainAs(text2) {
                centerTo(parent)
            })

            val barrier = createBottomBarrier(text1, text2)
            Text("This is a very long text", Modifier.constrainAs(text3) {
                top.linkTo(barrier, margin = 20.dp)
                centerHorizontallyTo(parent)
                width = Dimension.preferredWrapContent.atMost(40.dp)
            })
        }
    }

    @Composable
    fun DemoConstraintSet() {
        ConstraintLayout(ConstraintSet2 {
            val text1 = createRefFor("text1")
            val text2 = createRefFor("text2")
            val text3 = createRefFor("text3")

            constrain(text1) {
                start.linkTo(text2.end, margin = 20.dp)
            }
            constrain(text2) {
                centerTo(parent)
            }

            val barrier = createBottomBarrier(text1, text2)
            constrain(text3) {
                top.linkTo(barrier, margin = 20.dp)
                centerHorizontallyTo(parent)
                width = Dimension.preferredWrapContent.atMost(40.dp)
            }
        }) {
            Text("Text1", Modifier.tag("text1"))
            Text("Text2", Modifier.tag("text2"))
            Text("This is a very long text", Modifier.tag("text3"))
        }
    }
}