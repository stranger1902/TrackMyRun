package com.example.trackmyrun.service.domain.repository

import com.example.trackmyrun.service.domain.model.GpsLocationModel
import kotlinx.coroutines.flow.Flow

interface GpsManager {
    val currentGpsLocation: Flow<GpsLocationModel>
}