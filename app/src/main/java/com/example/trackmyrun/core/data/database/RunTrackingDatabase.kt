package com.example.trackmyrun.core.data.database

import com.example.trackmyrun.core.data.database.dao.RunDao
import com.example.trackmyrun.core.data.local.entity.RunEntity
import androidx.room.RoomDatabase
import androidx.room.Database

@Database(
    entities = [RunEntity::class],
    exportSchema = false,
    version = 1
)
abstract class RunTrackingDatabase: RoomDatabase() {

    abstract fun userDao(): RunDao

}