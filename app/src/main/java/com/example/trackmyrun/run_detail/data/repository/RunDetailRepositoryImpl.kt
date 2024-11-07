package com.example.trackmyrun.run_detail.data.repository

import com.example.trackmyrun.run_detail.domain.repository.RunDetailRepository
import com.example.trackmyrun.core.data.database.dao.RunDao
import com.example.trackmyrun.core.domain.model.RunModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RunDetailRepositoryImpl @Inject constructor(
    private val runDao: RunDao
): RunDetailRepository {

    override suspend fun getDetail(runId: String): RunModel? = withContext(Dispatchers.IO) {
        try {
            runDao.getRun(runId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}