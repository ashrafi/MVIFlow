package com.zoewave.myapplication.ui

//import androidx.ui.livedata.observeAsState
import android.util.Log
import androidx.compose.*
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.*
import androidx.ui.text.font.FontStyle
import androidx.ui.unit.TextUnit
import androidx.ui.unit.dp
import com.zoewave.myapplication.model.*
import com.zoewave.myapplication.room.Word
import javax.inject.Inject

class MainComposeUI @Inject constructor(private val vmAction : VMAction) {
    // Setup AmbientOf current state
    val CurrAppState = ambientOf<AppState> { AppState.Edit }


    @Composable
    fun HomeScreen(
        viewModel: WordViewModel
    ) {
        val currState by viewModel.state.collectAsState(initial = AppState.NotEdit)
        CurrAppState.provides(currState)
        Providers(CurrAppState provides currState) {
            ScaffoldWithBottomBarAndCutout(viewModel)
        }
    }

    @Composable
    fun ScaffoldWithBottomBarAndCutout(
        viewModel: WordViewModel
    ) {
        val scaffoldState = remember { ScaffoldState() }
        val words by viewModel.allWords.collectAsState(initial = emptyList())
        // both bottom app bar and FAB need to know this shape
        val fabShape = RoundedCornerShape(50)

        Scaffold(
            scaffoldState = scaffoldState,
            topAppBar = { TopAppBar(title = { Text("List of words") }) },
            bodyContent = { contentBody(words = words) },
            bottomAppBar = {
                bottomBar(
                    fabConfiguration = it,
                    fabShape = fabShape,
                    viewModel = viewModel
                )
            },
            floatingActionButton = { FAB(fabShape) },
            floatingActionButtonPosition = Scaffold.FabPosition.EndDocked
            //floatingActionButtonPosition = Scaffold.FabPosition.CenterDocked
        )
    }

    @Composable
    fun bottomBar(
        fabConfiguration: BottomAppBar.FabConfiguration?,
        fabShape: RoundedCornerShape,
        viewModel: WordViewModel
    ) {
        BottomAppBar(fabConfiguration = fabConfiguration, cutoutShape = fabShape) {
            IconButton(onClick = { vmAction.action(MVOperation.AddAPIWorld, viewModel) }) {
                Icon(Icons.Filled.Face)
            }
            IconButton(onClick = { vmAction.action(MVOperation.DeleteAll, viewModel) }) {
                Icon(Icons.Filled.Delete)
            }
            switchState(viewModel)
        }
    }

    @Composable
    fun contentBody(words: List<Word>) {
        Column() {
            AdapterList(data = words) { word ->
                Card(
                    color = Color.Cyan, //colors[it % colors.size],
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    if (CurrAppState.current == AppState.Edit) {
                        Text("${word.word}")
                    } else {
                        Text(
                            "no edit --> ${word.word}", color = Color.Gray,
                            fontStyle = FontStyle.Italic, fontSize = TextUnit.Sp(10)
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun switchState(viewModel: WordViewModel) {
        Switch(
            checked = (CurrAppState.current == AppState.Edit),
            onCheckedChange = {
                if (it) {
                    vmAction.action(MVOperation.CanEdit, viewModel)
                } else {
                    vmAction.action(MVOperation.NotEdit, viewModel)
                }
            },
            color = Color.Cyan
        )
        if (CurrAppState.current == AppState.NotEdit) {
            ShowAlert()
        }
    }

    @Composable
    fun FAB(
        fabShape: RoundedCornerShape
    ) {
        FloatingActionButton(
            onClick = {
                Log.v("MVI", "Under FAB: I got clicked")
            },
            // We specify the same shape that we passed as the cutoutShape above.
            shape = fabShape,
            backgroundColor = MaterialTheme.colors.secondary
        ) {
            IconButton(onClick = {
                navigateTo(NavScreen.AddWord)
            }) {
                if (CurrAppState.current == AppState.Edit)
                    Icon(asset = Icons.Filled.Add)
                else
                    Icon(asset = Icons.Filled.Close)
            }

        }
    }

    @Composable
    fun ShowAlert() {
        var showPopup by state { true }
        val onPopupDismissed = { showPopup = false }
        if (showPopup) {
            AlertDialog(
                onCloseRequest = onPopupDismissed,
                text = {
                    Text("System Edit is OFF")
                },
                confirmButton = {
                    Button(
                        onClick = onPopupDismissed
                    ) {
                        Text(text = "Ok")
                    }
                })
        }
    }
}