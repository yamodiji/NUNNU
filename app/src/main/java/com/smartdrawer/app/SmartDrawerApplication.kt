package com.smartdrawer.app

import android.app.Application
import android.content.Context

class SmartDrawerApplication : Application() {
    
    companion object {
        lateinit var instance: SmartDrawerApplication
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    fun getContext(): Context = applicationContext
} 