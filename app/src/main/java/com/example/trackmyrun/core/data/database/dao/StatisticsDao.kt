package com.example.trackmyrun.core.data.database.dao

import com.example.trackmyrun.core.domain.model.RunStatisticsModel
import androidx.room.Query
import androidx.room.Dao

@Dao
interface StatisticsDao {

    @Query("SELECT SUM(durationMillis) AS totalDurationMillis, " +
            "SUM(distanceMeters) AS totalDistanceMeters, " +
            "SUM(avgSpeedMs) AS totalAvgSpeedMs, " +
            "SUM(kcalBurned) AS totalKcalBurned, " +
            "COUNT(*) AS totalRun " +
            "FROM RUN"
    )
    fun getStatistics(): RunStatisticsModel

}