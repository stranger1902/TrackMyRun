package com.example.trackmyrun.service.di

import com.example.trackmyrun.service.data.repository.RunTrackingCountdownManager
import com.example.trackmyrun.service.data.repository.ServiceNotificationManager
import com.example.trackmyrun.service.data.repository.RunTrackingTimerManager
import com.example.trackmyrun.service.data.repository.RunTrackingGpsManager
import com.example.trackmyrun.service.domain.repository.CountdownManager
import com.example.trackmyrun.service.data.repository.RunTrackingManager
import com.example.trackmyrun.service.domain.repository.TimerManager
import com.example.trackmyrun.service.domain.repository.GpsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import com.google.android.gms.location.LocationServices
import com.example.trackmyrun.core.utils.UserManager
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
    fun provideLocationGpsManager(@ApplicationContext context: Context): GpsManager {
        return RunTrackingGpsManager(
            locationClient = LocationServices.getFusedLocationProviderClient(context)
        )
    }

    @Provides
    @Singleton
    fun provideRunTrackingManager(
        @ApplicationContext context: Context,
        gpsLocationManager: GpsManager,
        timerManager: TimerManager,
        userManager: UserManager
    ): RunTrackingManager {
        return RunTrackingManager(
            runTrackingGpsManager = gpsLocationManager,
            runTrackingTimerManager = timerManager,
            userManager = userManager,
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideCountdownManager(): CountdownManager {
        return RunTrackingCountdownManager()
    }

    @Provides
    @Singleton
    fun provideTimerManager(): TimerManager {
        return RunTrackingTimerManager()
    }

}