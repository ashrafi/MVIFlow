package com.zoewave.myapplication

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
open class MviFlowApp : Application() {
    init {
        Log.v("MVI", "This is the start")
    }
}
