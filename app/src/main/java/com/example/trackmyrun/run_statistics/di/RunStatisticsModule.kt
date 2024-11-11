package com.example.trackmyrun.run_statistics.di

import com.example.trackmyrun.run_statistics.data.repository.RunStatisticsRepositoryImpl
import com.example.trackmyrun.run_statistics.domain.repository.RunStatisticsRepository
import com.example.trackmyrun.core.data.database.dao.StatisticsDao
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.Provides
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object RunStatisticsModule {

    @Provides
    @Singleton
    fun provideRunStatisticsRepository(statisticsDao: StatisticsDao): RunStatisticsRepository {
        return RunStatisticsRepositoryImpl(statisticsDao)
    }

}