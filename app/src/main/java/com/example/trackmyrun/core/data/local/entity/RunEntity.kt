package com.example.trackmyrun.core.data.local.entity

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "RUN")
data class RunEntity(
    @PrimaryKey val id: String,
    val distanceMeters: Float,
    val startTimestamp: Long,
    val durationMillis: Long,
    val avgSpeedMs: Float,
    val kcalBurned: Float
)