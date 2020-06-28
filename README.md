This is almost done --  HARD HAT 👷‍♀️ Area. Please use Caution ⚠️  
<I>Most links below reference [code](https://github.com/ashrafi/MVIFlow) in this repo.</I>

# MVI KFlow
Repo for the 360|AnDev [Talk](https://360andev.com/sessions/composing-magic-with-jetpack-compose/) & 
[Slides](https://docs.google.com/presentation/d/105QxLVF57nKV5Q1Yaw5Zyk7on5E-Q3Ewid6U-5M8I00/edit?usp=sharing).     
Video code walkthrough link (coming soon)   

Detailed Description:

We take a standard MVVM Android Arch Component (AAC) project (Room, Repo, ModelView, Activities/XML) and replace them with "the new stuff".

Replacing:
* MVVM with MVI
* Activities/XML (navigation) with Compose (screens)
* Livedata with Kotlin Flow
* Workmanager with [CoroutineWorker](https://developer.android.com/topic/libraries/architecture/workmanager/advanced/coroutineworker)
* RxJava with Kolin ShareFlow (using Dagger -> Hilt DI).

## VMI

[MVI](http://hannesdorfmann.com/android/model-view-intent) is cyclic and produces an immutable state.

MVI fits RxJava because of the supporting functions but here we choose Kotlin [StateFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/).

> A <B>Flow</B> that represents a read-only state with a single updatable data value that emits updates to the value to its collectors. 
> The current value can be retrieved via value property. The flow of future updates to the value can be observed by collecting values from this flow.

> A <B>mutable state flow</B> is created using MutableStateFlow(value) constructor function with the initial value. The value of mutable state flow can be updated by setting its value property. Updates to the value are always conflated. So a slow collector skips fast updates, but always collects the most recently emitted value.
 
> <B>StateFlow</B> is useful as a data-model class to represent any kind of state. Derived values can be defined using various operators on the flows, with combine operator being especially useful to combine values from multiple state flows using arbitrary functions.

As thing progress Kotlin might support MVI constructs with [SharedFlow](https://github.com/Kotlin/kotlinx.coroutines/issues/2034) just like RxJava does.

> There is a need to have a Flow implementation that is hot (always active independently of collectors) and shares emitted values among all collectors that subscribe to it.
> * The simplest version of sharing operator builds a combination of a SharedFlow that downstream collects from and an upstream flow that emits into this SharedFlow   
  
This repo's MVI is patterned after this QuickBirks [post](https://quickbirdstudios.com/blog/android-mvi-kotlin-coroutines-flow/)

###### --- Using StateFlow for MVI ---
We have an [App State](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/AppState.kt#L15) which is -- can/can't [change](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/AppState.kt#L43) the Room DB with [Button Click](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/ui/AddWordComposeUI.kt#L119).

Setup a [channel](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/AppState.kt#L31) 
and a [state](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/AppState.kt#L39)
in the channel.  Whenever the user sends an [intent](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/VMAction.kt#L21)
the state is update through the [ViewModel](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/WordViewModel.kt).

The single source of state is used to give [access to the DB](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/ui/AddWordComposeUI.kt#L119), set the [state of the toggle](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/ui/MainComposeUI.kt#L110) and these can never be out of sync.

### MVI vs MVVM
Both SwiftUI/Combine Framework and Android Architectural Component build with MVVM.

MVVM is an excellent design patter and until Kotlin / Android support MVI out of the box, MVVM is a great solution.

We update the <I>state of the app</I> with <B>MVI</B>.    
But we <I>update the data</I> with <B>MVVM</B>. This is done with the standard [Room](https://github.com/ashrafi/MVIFlow/tree/master/app/src/main/java/com/zoewave/myapplication/room) -to> [Repo](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/WordRepo.kt) -to> [UI](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/ui/MainComposeUI.kt#L29)

Really like this [division](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/VMAction.kt#L13). Do <B>NOT</B> want to persist the edit state, but do want to persist the data.

Retrofit call adds to the word [list]()

## UI 
We see how easy it is to build a [list]() in Compose and set ui [dynaically](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/ui/MainComposeUI.kt#L91) 

Use [Coil]() do download image from web. 
Loading Network [images]()
---
#### constraints
Setup [constraints]()

## DI
Dagger [Hilt](https://github.com/ashrafi/MVIFlow/tree/master/app/src/main/java/com/zoewave/myapplication/di) is great. 
Just set the lifecycle by choosing which component to install [into]().

* It reduces boilerplate and makes scoping easy to work with.  

## Routing
Navigation [screen](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/AppState.kt#L59)


## Conclusion 

We see that using Kotlin for Android make development much faster / better. As both Compose and Kotlin evolve MVI might be built into the language.
