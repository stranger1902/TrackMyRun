package com.example.trackmyrun.service.data.repository

import com.example.trackmyrun.run_new.presentation.CurrentRunState
import com.example.trackmyrun.core.domain.model.PathPointModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RunTrackingManager @Inject constructor(
    private val gpsLocationManager: GpsLocationManager,
    private val timerManager: TimerManager
) {

    // flows used in RunTrackingService...
    val currentGpsLocation = gpsLocationManager.currentGpsLocation
    val timeElapsedMillis = timerManager.timeElapsedMillis

    private val _runTrackingState = MutableStateFlow(CurrentRunState())
    val runTrackingState = _runTrackingState.asStateFlow()

    fun updateRunTrackingState(pathPoint: PathPointModel, speedMs: Float, timeElapsedMillis: Long) {
        // TODO: aggiornare stato viewModel...




        _runTrackingState.update {
            it.copy(
                lastPathPoint = pathPoint
            )
        }
    }

    fun startTracking() {
        _runTrackingState.update {
            it.copy(
                pathPointList = it.pathPointList + emptyList(),
                isTracking = true
            )
        }.also {
            timerManager.startTimer()
        }
    }

    fun pauseTracking() {
        _runTrackingState.update {
            it.copy(
                isTracking = false
            )
        }.also {
            timerManager.pauseTimer()
        }
    }

    fun stopTracking() {
        _runTrackingState.update {
            it.copy(
                isTracking = false
            )
        }.also {
            timerManager.stopTimer()
        }
    }

}