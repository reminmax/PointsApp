package com.reminmax.pointsapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PointsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}