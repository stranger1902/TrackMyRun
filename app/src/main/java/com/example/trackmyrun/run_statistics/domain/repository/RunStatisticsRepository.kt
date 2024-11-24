package com.example.trackmyrun.run_statistics.domain.repository

import com.example.trackmyrun.core.domain.model.RunKcalBurnedModel
import com.example.trackmyrun.core.domain.model.RunStatisticsModel

interface RunStatisticsRepository {
    suspend fun getRunsKcalburned(): List<RunKcalBurnedModel>
    suspend fun getStatistics(): RunStatisticsModel?
}