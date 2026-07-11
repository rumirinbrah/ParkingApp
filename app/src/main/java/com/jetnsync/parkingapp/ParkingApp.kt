package com.jetnsync.parkingapp

import android.app.Application
import com.jetnsync.parkingapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ParkingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ParkingApp)
            modules(appModule)
        }
    }
}
