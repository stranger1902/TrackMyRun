package com.example.trackmyrun.main.di

import com.example.trackmyrun.main.data.repository.BottomNavigationRepositoryImpl
import com.example.trackmyrun.main.domain.repository.BottomNavigationRepository
import dagger.hilt.components.SingletonComponent
import dagger.hilt.InstallIn
import dagger.Provides
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideBottomNavigationRepository(): BottomNavigationRepository {
        return BottomNavigationRepositoryImpl()
    }

}