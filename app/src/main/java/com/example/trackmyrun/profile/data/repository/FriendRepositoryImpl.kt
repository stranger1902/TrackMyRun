package com.example.trackmyrun.profile.data.repository

import com.example.trackmyrun.profile.domain.repository.FriendRepository
import com.example.trackmyrun.core.data.local.database.dao.FriendDao
import com.example.trackmyrun.core.domain.model.ResourceModel
import com.example.trackmyrun.core.domain.model.FriendModel
import com.example.trackmyrun.core.domain.model.toEntity
import com.example.trackmyrun.core.domain.model.toModel
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendDao: FriendDao
): FriendRepository {

    override suspend fun getFriends(limit: Int, offset: Long?): ResourceModel<List<FriendModel>> {
        return try {
            ResourceModel.Success(
                data = friendDao.getFriends(limit, offset).map { it.toModel() }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResourceModel.Error(
                error = "Non è stato possibile recuperare gli amici",
                data = emptyList()
            )
        }
    }

    override suspend fun insertFriend(friend: FriendModel): Boolean {
        try {
            friendDao.insertFriend(friend.toEntity())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}