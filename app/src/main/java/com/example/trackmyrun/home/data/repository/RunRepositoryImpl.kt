package com.example.trackmyrun.home.data.repository

import com.example.trackmyrun.home.domain.repository.RunRepository
import com.example.trackmyrun.core.data.local.database.dao.RunDao
import com.example.trackmyrun.core.domain.model.ResourceModel
import com.example.trackmyrun.core.domain.model.RunModel
import com.example.trackmyrun.core.domain.model.toEntity
import com.example.trackmyrun.core.domain.model.toModel
import javax.inject.Inject

class RunRepositoryImpl @Inject constructor(
    private val runDao: RunDao
): RunRepository {

    override suspend fun getRuns(limit: Int, offset: Long?): ResourceModel<List<RunModel>> {
        return try {
            ResourceModel.Success(
                data = runDao.getRuns(limit, offset).map { it.toModel() },
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResourceModel.Error(
                error = "Non è stato possibile recuperare le corse",
                data = emptyList()
            )
        }
    }

    override suspend fun insertRun(run: RunModel): Boolean {
        try {
            runDao.insertRun(run.toEntity())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override suspend fun deleteRun(run: RunModel): Boolean {
        try {
            runDao.deleteRun(run.toEntity())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}