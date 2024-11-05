package com.example.trackmyrun.run_new.presentation

import com.example.trackmyrun.core.domain.model.PathPointModel

data class CurrentRunState(
    val pathPointList: List<MutableList<PathPointModel>> = emptyList(),
    val lastPathPoint: PathPointModel? = null,
    val timeElapsedMillis: Long = 0L,
    val isTracking: Boolean = false,
    val distanceMeters: Float = 0f,
    val avgSpeedMs: Float = 0f,
    val kcalBurned: Float = 0f
)