package com.example.trackmyrun.bluetooth.di

import com.example.trackmyrun.bluetooth.data.chat.AndroidBluetoothController
import com.example.trackmyrun.bluetooth.domain.chat.BluetoothController
import com.example.trackmyrun.core.utils.PermissionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import android.content.Context
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.Provides
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {

    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context, permissionManager: PermissionManager): BluetoothController {
        return AndroidBluetoothController(context, permissionManager)
    }

}