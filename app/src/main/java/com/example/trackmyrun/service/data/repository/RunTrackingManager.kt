package com.example.trackmyrun.service.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class RunTrackingManager @Inject constructor(
    private val locationGpsManager: LocationGpsManager
) {

    val currentGpsLocation = locationGpsManager.currentGpsLocation

    private val _runTrackingState = MutableStateFlow(0)
    val runTrackingState = _runTrackingState.asStateFlow()

    fun updateRunTrackingState() {
        // TODO: aggiornare stato viewModel...



    }

    fun startTracking() {

    }

    fun pauseTracking() {

    }

    fun stopTracking() {

    }

}