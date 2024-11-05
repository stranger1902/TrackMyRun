package com.example.trackmyrun.run_new.presentation

import com.example.trackmyrun.core.domain.model.PathPointModel

data class CurrentRunState(
    val pathPointList: List<List<PathPointModel>> = emptyList(),
    val lastPathPoint: PathPointModel? = null,
    val isTracking: Boolean = false,
    val distanceMeters: Int = 0,
    val kcalBurned: Float = 0f,
    val speedKmH: Float = 0f
)