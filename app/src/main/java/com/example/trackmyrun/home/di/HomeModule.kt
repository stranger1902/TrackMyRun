package com.example.trackmyrun.home.di

import com.example.trackmyrun.home.data.repository.RunRepositoryImpl
import com.example.trackmyrun.home.domain.repository.RunRepository
import com.example.trackmyrun.core.data.local.database.dao.RunDao
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.Provides
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideRunRepository(runDao: RunDao): RunRepository {
        return RunRepositoryImpl(runDao)
    }

}