package com.example.trackmyrun.on_boarding.di

import com.example.trackmyrun.on_boarding.data.repository.OnBoardingRepositoryImpl
import com.example.trackmyrun.on_boarding.domain.repository.OnBoardingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import android.content.Context
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.Provides
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object OnBoardingModule {

    @Provides
    @Singleton
    fun provideOnBoardingRepository(@ApplicationContext context: Context): OnBoardingRepository {
        return OnBoardingRepositoryImpl(context)
    }

}