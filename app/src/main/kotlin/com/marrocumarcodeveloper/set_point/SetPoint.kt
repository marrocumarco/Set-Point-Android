package com.marrocumarcodeveloper.set_point

import android.app.Application

class SetPoint: Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}