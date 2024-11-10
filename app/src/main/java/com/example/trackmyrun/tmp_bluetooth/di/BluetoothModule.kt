package com.example.trackmyrun.tmp_bluetooth.di

import com.example.trackmyrun.tmp_bluetooth.data.chat.AndroidBluetoothController
import com.example.trackmyrun.tmp_bluetooth.domain.chat.BluetoothController
import dagger.hilt.components.SingletonComponent
import android.content.Context
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.Provides
import dagger.Module
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {

    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController {
        return AndroidBluetoothController(context)
    }

}