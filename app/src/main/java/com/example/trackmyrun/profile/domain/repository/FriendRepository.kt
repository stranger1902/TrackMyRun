package com.example.trackmyrun.profile.domain.repository

import com.example.trackmyrun.core.domain.model.ResourceModel
import com.example.trackmyrun.core.domain.model.FriendModel

interface FriendRepository {
    suspend fun getFriends(limit: Int, offset: Long?): ResourceModel<List<FriendModel>>
    suspend fun insertFriend(friend: FriendModel): Boolean
}