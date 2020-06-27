package com.zoewave.myapplication.ui

//import androidx.ui.livedata.observeAsState
import android.util.Log
import androidx.compose.*
import androidx.core.graphics.drawable.toBitmap
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Add
import androidx.ui.material.icons.filled.Close
import androidx.ui.material.icons.filled.Delete
import androidx.ui.material.icons.filled.Face
import androidx.ui.text.font.FontStyle
import androidx.ui.unit.TextUnit
import androidx.ui.unit.dp
import androidx.ui.viewmodel.viewModel
import coil.Coil
import coil.request.GetRequest
import com.zoewave.myapplication.model.*
import com.zoewave.myapplication.room.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainComposeUI @Inject constructor(private val vmAction : VMAction) {
    // Setup AmbientOf current state
    val CurrAppState = ambientOf<AppState> { AppState.Edit }

    @Composable
    fun HomeScreen() {
        val wordViewModel = viewModel<WordViewModel>()
        val currState by wordViewModel.state.collectAsState(initial = AppState.NotEdit)
        CurrAppState.provides(currState)
        Providers(CurrAppState provides currState) {
            ScaffoldWithBottomBarAndCutout(wordViewModel)
        }
    }

    @Composable
    fun ScaffoldWithBottomBarAndCutout(wordViewModel: WordViewModel) {
        val scaffoldState = remember { ScaffoldState() }
        val words by wordViewModel.allWords.collectAsState(initial = emptyList())
        // both bottom app bar and FAB need to know this shape
        val fabShape = RoundedCornerShape(50)

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopAppBar(title = { Text("List of words") }) },
            bodyContent = { contentBody(words = words) },
            bottomBar = {
                bottomBar()
            },
            floatingActionButton = { FAB(fabShape) },
            floatingActionButtonPosition = Scaffold.FabPosition.End
            //floatingActionButtonPosition = Scaffold.FabPosition.CenterDocked
        )
    }

    @Composable
    fun bottomBar() {
        var showPopup by state { false }
        val onPopupDismissed = { showPopup = false }
        val wordViewModel = viewModel<WordViewModel>()
        BottomAppBar {
            IconButton(onClick = {
                showPopup = true
            }) { Icon(Icons.Filled.Face) }
            IconButton(onClick = { vmAction.action(MVOperation.DeleteAll, wordViewModel) }) {
                Icon(Icons.Filled.Delete)
            }
            switchState(wordViewModel)
            if (showPopup) {
                AlertDialog(
                    onCloseRequest = onPopupDismissed,
                    text = {
                        Column {
                            Text("Network Image")
                            showImage()
                        }
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


    @Composable
    fun contentBody(words: List<Word>) {
        Column {
            LazyColumnItems(items = words, itemContent = { word ->
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
            })
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
        if (CurrAppState.current == AppState.NotEdit)
            ShowAlert()
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
                    Column {
                        Text("System Edit is OFF")
                    }
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

    @Composable
    fun showImage() {
        val image = state<ImageAsset> { ImageAsset(24, 24) }
        val image_data =
            "https://www.sail-world.com/photos/sailworld/photos/Large_610577174_SD5_3584.jpg"

        val request = GetRequest.Builder(ContextAmbient.current)
            .data(image_data)
            //.transformations(CircleCropTransformation())
            .build()

        CoroutineScope(Dispatchers.Main.immediate).launch {
            // Start loading the image and await the result.
            val drawable = Coil.execute(request).drawable
            image.value = drawable?.toBitmap(
                width = drawable.intrinsicWidth,
                height = drawable.intrinsicHeight
            )!!.asImageAsset()
        }

        Image(asset = image.value)

    }

}