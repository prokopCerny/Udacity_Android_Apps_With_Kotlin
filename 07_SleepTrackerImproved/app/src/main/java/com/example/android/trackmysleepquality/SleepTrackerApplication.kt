package com.example.android.trackmysleepquality

import android.app.Application
import timber.log.Timber

class SleepTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}