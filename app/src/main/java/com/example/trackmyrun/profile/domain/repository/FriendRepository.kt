package com.example.trackmyrun.profile.domain.repository

import com.example.trackmyrun.core.data.local.model.ResponsePagingModel
import com.example.trackmyrun.core.domain.model.FriendModel

interface FriendRepository {
    suspend fun getFriends(limit: Int, offset: Long?): ResponsePagingModel<FriendModel>
    suspend fun insertFriend(friend: FriendModel): Boolean
}