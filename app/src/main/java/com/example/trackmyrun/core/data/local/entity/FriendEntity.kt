package com.example.trackmyrun.core.data.local.entity

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "FRIEND")
data class FriendEntity(
    @PrimaryKey val id: String,
    val startTimestamp: Long,
    val name: String
)