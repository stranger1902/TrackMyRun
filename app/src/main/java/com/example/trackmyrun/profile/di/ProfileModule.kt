package com.example.trackmyrun.profile.di

import com.example.trackmyrun.profile.data.repository.FriendRepositoryImpl
import com.example.trackmyrun.profile.domain.repository.FriendRepository
import com.example.trackmyrun.core.data.local.database.dao.FriendDao
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.Provides
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideFriendRepository(friendDao: FriendDao): FriendRepository {
        return FriendRepositoryImpl(friendDao)
    }

}