This is ready for public consumption, but HARD HAT 👷‍♀️ Area. Please use Caution ⚠️  
# MVI KFlow
Repo for the 360|AnDev [Talk](https://360andev.com/sessions/composing-magic-with-jetpack-compose/) &
[Slides](https://docs.google.com/presentation/d/105QxLVF57nKV5Q1Yaw5Zyk7on5E-Q3Ewid6U-5M8I00/edit?usp=sharing).     
Video code walkthrough link (Posted shortly after presentation)   

Detailed Description:

We take a standard MVVM Android Arch Component (AAC) project (Room, Repo, ModelView, Activities/XML) and replace them with "the new stuff".

Replacing:
* MVVM with MVI
* Activities/XML (navigation) with Compose (screens)
* Livedata with Kotlin Flow
* Workmanager with [CoroutineWorker](https://developer.android.com/topic/libraries/architecture/workmanager/advanced/coroutineworker)
* RxJava with Kolin ShareFlow

Also adding
* Dagger Hilt
* Coil

## VMI

[MVI](http://hannesdorfmann.com/android/model-view-intent) is cyclic and produces an immutable state.

MVI fits RxJava because of the supporting functions but here we choose Kotlin [StateFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/).

> A <B>Flow</B> that represents a read-only state with a single updatable data value that emits updates to the value to its collectors.
> The current value can be retrieved via value property. The flow of future updates to the value can be observed by collecting values from this flow.

> A <B>mutable state flow</B> is created using MutableStateFlow(value) constructor function with the initial value. The value of mutable state flow can be updated by setting its value property. Updates to the value are always conflated. So a slow collector skips fast updates, but always collects the most recently emitted value.

> <B>StateFlow</B> is useful as a data-model class to represent any kind of state. Derived values can be defined using various operators on the flows, with combine operator being especially useful to combine values from multiple state flows using arbitrary functions.

As thing progress Kotlin might support MVI constructs with something like StateFlow combined with [SharedFlow](https://github.com/Kotlin/kotlinx.coroutines/issues/2034) just like RxJava does.

> There is a need to have a Flow implementation that is hot (always active independently of collectors) and shares emitted values among all collectors that subscribe to it.
> * The simplest version of sharing operator builds a combination of a SharedFlow that downstream collects from and an upstream flow that emits into this SharedFlow   

This repo's MVI is patterned after this QuickBirks [post](https://quickbirdstudios.com/blog/android-mvi-kotlin-coroutines-flow/)

###### --- Using StateFlow for MVI ---
We have an View State

```
data class ViewState(
    val progress: Boolean = false,
    val error: Boolean = false,
    var appState: AppState = AppState.Edit,
    val allWords: Flow<List<Word>> = flowOf(ArrayList<Word>().toList())
)
```

which has an App State  -- can/can't change
```
sealed class AppState {
    object Edit : AppState()
    object NotEdit : AppState()
}
```
Setup a channel  
`val userIntentChannel = Channel<UserIntent>()`

and a state  
`private val _state = MutableStateFlow(ViewState())`
in the channel.  

Whenever the user sends an intent the state is update through the ViewModel.

The single source of state is used to give access to the DB, set the state of the toggle and these can never be out of sync.

### MVI vs MVVM
Both SwiftUI/Combine Framework and Android Architectural Component build with MVVM.

MVVM is an excellent design patter and until Kotlin / Android support MVI out of the box, MVVM is a great solution.

MVI adds to MVVM a channel, reducer and an immutable ViewState that is rendered by the view Whenever it changes.

### UI
We see how easy it is to build a list
```
Column {
            LazyColumnItems(items = words, itemContent = { word ->
                Card(
                    color = Color.Cyan, //colors[it % colors.size],
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
```

in Compose and set ui dynaically
```
if (CurrAppState.current.appState == AppState.Edit) {
                       Text("${word.word}")
                   } else {
                       Text(
                           "no edit --> ${word.word}", color = Color.Gray,
                           fontStyle = FontStyle.Italic, fontSize = TextUnit.Sp(10)
                       )
                   }
```                   
#### constraints
Setup constraints
```
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
```


### DI
Dagger Hilt is great.
Just set the lifecycle by choosing which component to install into.
```
// application scope
@InstallIn(ApplicationComponent::class)
@Module
object StateModule {
```  
* It reduces boilerplate and makes scoping easy to work with.  

## Routing
Navigation is done by flipping out screens (Composable)
```
setContent {
            Column {
                AppTheme {
                    Text("--> Place Holder - Nav is below here <--")
                    AppContent(mainComposeUI, addWordComposeUI)
                }
            }
        }
```        


# Conclusion

We see that using Kotlin for Android make development much faster / better. As both Compose and Kotlin evolve MVI might be built into the language.
