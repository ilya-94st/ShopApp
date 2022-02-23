package com.example.shopapp.presentation.ui

import android.annotation.SuppressLint
import android.app.Application
import com.example.shopapp.common.SharedPref

val prefs: SharedPref by lazy {
    BaseApplication.prefs!!
}

class BaseApplication: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var prefs: SharedPref? = null
        lateinit var instance: BaseApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        prefs = SharedPref(applicationContext)
    }
}