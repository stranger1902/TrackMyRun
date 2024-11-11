package com.example.trackmyrun.core.data.database

import com.example.trackmyrun.core.data.database.dao.StatisticsDao
import com.example.trackmyrun.core.data.local.entity.FriendEntity
import com.example.trackmyrun.core.data.database.dao.FriendDao
import com.example.trackmyrun.core.data.local.entity.RunEntity
import com.example.trackmyrun.core.data.database.dao.RunDao
import androidx.room.RoomDatabase
import androidx.room.Database

@Database(
    entities = [RunEntity::class, FriendEntity::class],
    exportSchema = false,
    version = 1
)
abstract class RunTrackingDatabase: RoomDatabase() {

    abstract fun statisticsDao(): StatisticsDao
    abstract fun friendDao(): FriendDao
    abstract fun runDao(): RunDao

}