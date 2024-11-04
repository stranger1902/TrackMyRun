package com.example.trackmyrun

import com.example.trackmyrun.service.data.repository.ServiceNotificationManager
import dagger.hilt.android.HiltAndroidApp
import android.app.Application
import javax.inject.Inject

@HiltAndroidApp
class MyApp: Application() {

    @Inject
    lateinit var notificationManager: ServiceNotificationManager

    override fun onCreate() {

        super.onCreate()

        notificationManager.createNotificationChannel()
    }
}