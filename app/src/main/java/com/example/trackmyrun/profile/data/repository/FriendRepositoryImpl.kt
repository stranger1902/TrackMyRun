package com.example.trackmyrun.profile.data.repository

import com.example.trackmyrun.profile.domain.repository.FriendRepository
import com.example.trackmyrun.core.data.local.model.ResponsePagingModel
import com.example.trackmyrun.core.data.local.model.ResponseErrorModel
import com.example.trackmyrun.core.data.database.dao.FriendDao
import com.example.trackmyrun.core.domain.model.FriendModel
import com.example.trackmyrun.core.domain.model.toEntity
import com.example.trackmyrun.core.domain.model.toModel
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendDao: FriendDao
): FriendRepository {

    override suspend fun getFriends(limit: Int, offset: Long?): ResponsePagingModel<FriendModel> {
        return try {
            ResponsePagingModel(
                data = friendDao.getFriends(limit, offset).map { it.toModel() },
                error = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponsePagingModel(
                data = emptyList(),
                error = ResponseErrorModel(
                    message = "Non è stato possibile recuperare gli amici",
                    errorCode = null
                )
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