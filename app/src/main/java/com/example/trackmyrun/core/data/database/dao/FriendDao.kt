package com.example.trackmyrun.core.data.database.dao

import com.example.trackmyrun.core.data.local.entity.FriendEntity
import androidx.room.Upsert
import androidx.room.Query
import androidx.room.Dao

@Dao
interface FriendDao {

    @Query("SELECT * FROM FRIEND ORDER BY startTimestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getFriends(limit: Int, offset: Long?): List<FriendEntity>

    @Upsert
    suspend fun insertFriend(friendEntity: FriendEntity)
}