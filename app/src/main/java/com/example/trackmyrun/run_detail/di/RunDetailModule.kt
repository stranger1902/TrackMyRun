package com.example.trackmyrun.run_detail.di

import com.example.trackmyrun.run_detail.data.repository.RunDetailRepositoryImpl
import com.example.trackmyrun.run_detail.domain.repository.RunDetailRepository
import com.example.trackmyrun.core.data.local.database.dao.RunDao
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.Provides
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object RunDetailModule {

    @Provides
    @Singleton
    fun provideRunDetailRepository(runDao: RunDao): RunDetailRepository {
        return RunDetailRepositoryImpl(runDao)
    }

}