package com.example.trackmyrun.core.di

import com.example.trackmyrun.core.data.database.RunTrackingDatabase
import com.example.trackmyrun.core.utils.FileImageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.trackmyrun.core.utils.UserManager
import com.example.trackmyrun.core.utils.Constants
import dagger.hilt.components.SingletonComponent
import android.content.Context
import javax.inject.Singleton
import dagger.hilt.InstallIn
import androidx.room.Room
import dagger.Provides
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object OnBoardingModule {

    @Provides
    @Singleton
    fun provideFileImageManager(@ApplicationContext context: Context): FileImageManager {
        return FileImageManager(context)
    }

    @Provides
    @Singleton
    fun provideUserManager(@ApplicationContext context: Context): UserManager {
        return UserManager(context)
    }

    fun provideRunTrackingDatabase(@ApplicationContext context: Context): RunTrackingDatabase {
        return Room.databaseBuilder(
            klass = RunTrackingDatabase::class.java,
            name = Constants.RUN_TRACKING_DB_NAME,
            context = context
        ).build()
    }

}