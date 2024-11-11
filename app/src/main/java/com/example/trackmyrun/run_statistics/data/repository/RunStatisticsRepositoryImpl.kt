package com.example.trackmyrun.run_statistics.data.repository

import com.example.trackmyrun.run_statistics.domain.repository.RunStatisticsRepository
import com.example.trackmyrun.core.data.database.dao.StatisticsDao
import com.example.trackmyrun.core.domain.model.RunStatisticsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RunStatisticsRepositoryImpl
@Inject constructor(
    private val statisticsDao: StatisticsDao
): RunStatisticsRepository {

    override suspend fun getStatistics(): RunStatisticsModel? = withContext(Dispatchers.IO) {
        try {
            statisticsDao.getStatistics()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}