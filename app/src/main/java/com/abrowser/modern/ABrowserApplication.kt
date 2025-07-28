package com.abrowser.modern

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ABrowserApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}