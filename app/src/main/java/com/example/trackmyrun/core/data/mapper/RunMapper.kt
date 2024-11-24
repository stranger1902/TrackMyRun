package com.example.trackmyrun.core.data.mapper

import com.example.trackmyrun.core.data.local.entity.RunEntity
import com.example.trackmyrun.core.domain.model.RunModel

fun RunEntity.toModel(): RunModel = RunModel(
    distanceMeters = distanceMeters,
    startTimestamp = startTimestamp,
    durationMillis = durationMillis,
    avgSpeedMs = avgSpeedMs,
    kcalBurned = kcalBurned,
    id = id
)

fun RunModel.toEntity(): RunEntity = RunEntity(
    distanceMeters = distanceMeters,
    startTimestamp = startTimestamp,
    durationMillis = durationMillis,
    avgSpeedMs = avgSpeedMs,
    kcalBurned = kcalBurned,
    id = id
)