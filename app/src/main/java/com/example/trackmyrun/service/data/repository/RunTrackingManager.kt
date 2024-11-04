package com.example.trackmyrun.service.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class RunTrackingManager @Inject constructor(
    private val locationGpsManager: LocationGpsManager,
    private val timerManager: TimerManager
) {

    val currentGpsLocation = locationGpsManager.currentGpsLocation

    val timeElapsedMillis = timerManager.timeElapsedMillis

    private val _runTrackingState = MutableStateFlow(0)
    val runTrackingState = _runTrackingState.asStateFlow()

    fun updateRunTrackingState() {
        // TODO: aggiornare stato viewModel...



    }

    fun startTracking() {
        // TODO: aggiornare stato viewModel...


        timerManager.startTimer()
    }

    fun pauseTracking() {
        // TODO: aggiornare stato viewModel...


        timerManager.pauseTimer()
    }

    fun stopTracking() {
        // TODO: aggiornare stato viewModel...


        timerManager.stopTimer()
    }

}