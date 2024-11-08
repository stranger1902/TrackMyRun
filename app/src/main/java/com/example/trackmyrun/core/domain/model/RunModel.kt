package com.example.trackmyrun.core.domain.model

data class RunModel(
    val distanceMeters: Float,
    val startTimestamp: Long,
    val durationMillis: Long,
    val avgSpeedMs: Float,
    val kcalBurned: Float,
    val id: String
)