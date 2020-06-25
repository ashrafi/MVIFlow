# MVI KFlow
Repo for the 360|AnDev [Talk](https://360andev.com/sessions/composing-magic-with-jetpack-compose/) & 
[Slides](https://docs.google.com/presentation/d/105QxLVF57nKV5Q1Yaw5Zyk7on5E-Q3Ewid6U-5M8I00/edit?usp=sharing).     
Video code walkthrough link (coming soon)   
THIS SECTION IS STILL UNDER CONSTRUCTION ... HARD HAT 👷‍♀️ Area. Please use Caution ⚠️

Detailed Description:

We take a standard MVVM Android Arch Component (AAC) project (Room, Repo, ModelView, Activities/XML) and replace them with "the new stuff".

Replacing:
* MVVM with MVI
* Activities/XML (navigation) with Compose (screens)
* Livedata with Kotlin Flow
* Workmanager with [CoroutineWorker](https://developer.android.com/topic/libraries/architecture/workmanager/advanced/coroutineworker)
* RxJava with Kolin ShareFlow (using Dagger -> Hilt DI).

---

MVI is patterned after this QuickBirks [post](https://quickbirdstudios.com/blog/android-mvi-kotlin-coroutines-flow/)

We have an[ App State](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/AppState.kt#L15) which is -- can/can't [change](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/AppState.kt#L43) the Room DB with [Button Click](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/ui/AddWordComposeUI.kt#L119).

Setup a [channel](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/AppState.kt#L31) 
and a [state](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/AppState.kt#L39)
in the channel.  Whenever the user sends an [intent](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/VMAction.kt#L21)
the state is update through the [ViewModel](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/WordViewModel.kt).

The single source of state is used to give [access to the DB](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/ui/AddWordComposeUI.kt#L119), set the [state of the toggle](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/ui/MainComposeUI.kt#L110) and these can never be out of sync.

We update the state of the app with MVI.    
But we update the data with MVVM. This is done with the standard [Room](https://github.com/ashrafi/MVIFlow/tree/master/app/src/main/java/com/zoewave/myapplication/room) -to> [Repo](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/WordRepo.kt) -to> [UI](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/ui/MainComposeUI.kt#L29)


Really like this [division](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/VMAction.kt#L13). Do <B>NOT</B> want to persist the edit state, but do want to persist the data.

--- 
We see how easy it is to build a [list] in Compose and set ui [dynaically](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/ui/MainComposeUI.kt#L91) 

---
Dagger [Hilt](https://github.com/ashrafi/MVIFlow/tree/master/app/src/main/java/com/zoewave/myapplication/di) is great. It reduces boilerplate and makes scoping easy to work with.  Just set the lifecycle by chosing which component to intsall into.

---
Navigation [screen](https://github.com/ashrafi/MVIFlow/blob/master/app/src/main/java/com/zoewave/myapplication/model/AppState.kt#L59)




