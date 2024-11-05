package com.example.trackmyrun.service.data.repository

import com.example.trackmyrun.run_new.presentation.CurrentRunState
import com.example.trackmyrun.core.domain.model.PathPointModel
import com.example.trackmyrun.core.domain.model.toLatLng
import com.example.trackmyrun.core.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.pow

class RunTrackingManager @Inject constructor(
    private val gpsLocationManager: GpsLocationManager,
    private val timerManager: TimerManager,
    private val userManager: UserManager
) {

    // flows launched in RunTrackingService scope...
    val currentGpsLocation = gpsLocationManager.currentGpsLocation
    val timeElapsedMillis = timerManager.timeElapsedMillis

    private val _runTrackingState = MutableStateFlow(CurrentRunState())
    val runTrackingState = _runTrackingState.asStateFlow()

    fun updateRunTrackingState(pathPoint: PathPointModel, speedMs: Float, timeElapsedMillis: Long) {

        val size = _runTrackingState.value.pathPointList.sumOf { it.size }

        val speedKmH = speedMs / 3.6

        val calories = (timeElapsedMillis/3600f) * (userManager.currentUser.value.weight * speedKmH * 0.035 + speedKmH.pow(2) * 0.029) / 60

        val distance = _runTrackingState.value.lastPathPoint?.let { lastPathPoint ->
            SphericalUtil.computeDistanceBetween(lastPathPoint.toLatLng(), pathPoint.toLatLng())
        } ?: 0.0

        _runTrackingState.update {
            it.copy(
                avgSpeedMs = ((_runTrackingState.value.avgSpeedMs * size) + speedMs) / (size + 1),
                distanceMeters = it.distanceMeters + distance.toFloat(),
                kcalBurned = it.kcalBurned + calories.toFloat(),
                timeElapsedMillis = timeElapsedMillis,
                lastPathPoint = pathPoint,
                pathPointList = _runTrackingState.value.pathPointList.apply {
                    last().add(pathPoint)
                }
            )
        }
    }

    fun startTracking() {
        _runTrackingState.update {
            it.copy(
                pathPointList = it.pathPointList.toMutableList().apply { add(mutableListOf()) },
                isTracking = true
            )
        }.also {
            timerManager.startTimer()
        }
    }

    fun pauseTracking() {
        _runTrackingState.update {
            it.copy(
                lastPathPoint = null,
                isTracking = false
            )
        }.also {
            timerManager.pauseTimer()
        }
    }

    fun stopTracking() {
        _runTrackingState.update {
            CurrentRunState()
        }.also {
            timerManager.stopTimer()
        }
    }

}