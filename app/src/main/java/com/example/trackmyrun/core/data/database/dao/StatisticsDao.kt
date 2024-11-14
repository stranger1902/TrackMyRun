package com.example.trackmyrun.core.data.database.dao

import com.example.trackmyrun.core.domain.model.RunStatisticsModel
import com.example.trackmyrun.core.domain.model.RunKcalBurnedModel
import androidx.room.Query
import androidx.room.Dao

@Dao
interface StatisticsDao {

    @Query("SELECT SUM(durationMillis) AS totalDurationMillis, " +
            "SUM(distanceMeters) AS totalDistanceMeters, " +
            "AVG(avgSpeedMs) AS totalAvgSpeedMs, " +
            "SUM(kcalBurned) AS totalKcalBurned, " +
            "COUNT(*) AS totalRun " +
            "FROM RUN")
    suspend fun getStatistics(): RunStatisticsModel

    @Query("SELECT kcalBurned AS kcalBurned, startTimestamp AS startTimestamp FROM RUN")
    suspend fun getRunsKcalburned(): List<RunKcalBurnedModel>
}