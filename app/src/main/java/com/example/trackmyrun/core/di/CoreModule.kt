package com.example.trackmyrun.core.di

import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.trackmyrun.core.utils.UserManager
import dagger.hilt.components.SingletonComponent
import android.content.Context
import dagger.hilt.InstallIn
import dagger.Provides
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object OnBoardingModule {

    @Provides
    fun provideUserManager(@ApplicationContext context: Context): UserManager {
        return UserManager(context)
    }

}