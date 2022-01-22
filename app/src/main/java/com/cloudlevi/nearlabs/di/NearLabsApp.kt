package com.cloudlevi.nearlabs.di

import android.app.Application

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NearLabsApp: Application() {
    companion object {
        lateinit var instance: NearLabsApp
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}