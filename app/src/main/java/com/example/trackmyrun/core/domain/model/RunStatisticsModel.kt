package com.example.trackmyrun.core.domain.model

data class RunStatisticsModel(
    val totalDistanceMeters: Float,
    val totalDurationMillis: Long,
    val totalKcalBurned: Float,
    val totalAvgSpeedMs: Float,
    val totalRun: Int
)