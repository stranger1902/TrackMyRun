package com.example.trackmyrun.service.domain.model

import com.example.trackmyrun.core.domain.model.PathPointModel

data class GpsLocationModel(
    val pathPoint: PathPointModel,
    val speedMs: Float
)