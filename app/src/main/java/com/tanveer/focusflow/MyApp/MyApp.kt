package com.tanveer.focusflow.MyApp

import android.app.Application
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()

        OneSignal.initWithContext(this)
    }
}
