package com.example.trackmyrun.core.domain.model

import com.example.trackmyrun.core.data.local.entity.RunEntity
import com.google.android.gms.maps.model.LatLng

fun PathPointModel.toLatLng(): LatLng = LatLng(latitude, longitude)

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