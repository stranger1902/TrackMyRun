package com.example.trackmyrun.run_statistics.domain.repository

import com.example.trackmyrun.core.domain.model.RunStatisticsModel

interface RunStatisticsRepository {
    suspend fun getStatistics(): RunStatisticsModel?
}