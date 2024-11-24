package com.example.trackmyrun.service.domain.model

data class TrackingInfoModel(
    val gpsLocation: GpsLocationModel,
    val timeElapsedMillis: Long,
    val isGpsEnabled: Boolean
)