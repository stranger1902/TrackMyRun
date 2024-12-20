package com.example.trackmyrun.core.bluetooth.di

import com.example.trackmyrun.core.bluetooth.domain.interfaces.BluetoothController
import com.example.trackmyrun.core.bluetooth.AndroidBluetoothController
import com.example.trackmyrun.core.utils.PermissionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.trackmyrun.core.utils.UserManager
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
    fun provideBluetoothController(
        @ApplicationContext context: Context,
        permissionManager: PermissionManager,
        userManager: UserManager
    ): BluetoothController {
        return AndroidBluetoothController(context, permissionManager, userManager)
    }

}