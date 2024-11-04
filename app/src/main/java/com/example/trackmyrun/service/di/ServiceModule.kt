package com.example.trackmyrun.service.di

import com.example.trackmyrun.service.data.repository.ServiceNotificationManager
import com.example.trackmyrun.service.data.repository.LocationGpsManager
import com.example.trackmyrun.service.data.repository.TimerManager
import dagger.hilt.android.qualifiers.ApplicationContext
import com.google.android.gms.location.LocationServices
import dagger.hilt.components.SingletonComponent
import android.content.Context
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.Provides
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideServiceNotificationManager(@ApplicationContext context: Context): ServiceNotificationManager {
        return ServiceNotificationManager(context)
    }

    @Provides
    @Singleton
    fun provideLocationGpsManager(@ApplicationContext context: Context): LocationGpsManager {
        return LocationGpsManager(
            locationClient = LocationServices.getFusedLocationProviderClient(context),
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideTimerManager(): TimerManager {
        return TimerManager()
    }

}