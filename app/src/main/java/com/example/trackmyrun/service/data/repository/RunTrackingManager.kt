package com.example.trackmyrun.service.data.repository

import com.example.trackmyrun.service.domain.repository.TimerManager
import com.example.trackmyrun.service.domain.repository.GpsManager
import com.example.trackmyrun.run_new.presentation.CurrentRunState
import com.example.trackmyrun.core.domain.model.PathPointModel
import com.example.trackmyrun.service.LocationStateReceiver
import com.example.trackmyrun.core.domain.mapper.toLatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.trackmyrun.core.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.flow.asStateFlow
import android.location.LocationManager
import kotlinx.coroutines.flow.update
import android.content.IntentFilter
import android.content.Context
import javax.inject.Inject
import kotlin.math.pow

class RunTrackingManager @Inject constructor(
    private val runTrackingTimerManager: TimerManager,
    @ApplicationContext private val context: Context,
    private val runTrackingGpsManager: GpsManager,
    private val userManager: UserManager
) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // flows launched in RunTrackingService scope...
    val currentGpsLocation = runTrackingGpsManager.currentGpsLocation
    val timeElapsedMillis = runTrackingTimerManager.timeElapsedMillis

    private val _runTrackingState = MutableStateFlow(CurrentRunState())
    val runTrackingState = _runTrackingState.asStateFlow()

    // flow launched in RunTrackingService scope...
    private val _isGpsEnabled = MutableStateFlow(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    val isGpsEnabled = _isGpsEnabled.asStateFlow()

    private val locationStateReceiver = LocationStateReceiver { isGpsEnabled ->
        _isGpsEnabled.update { isGpsEnabled }
    }

    fun updateRunTrackingState(pathPoint: PathPointModel, speedMs: Float, timeElapsedMillis: Long) {

        val size = _runTrackingState.value.pathPointList.sumOf { it.size }

        val speedKmH = speedMs / 3.6

        val calories = (timeElapsedMillis/3600f) * (userManager.currentUser.value.weight * speedKmH * 0.035 + speedKmH.pow(2) * 0.029) / 60

        val distance = _runTrackingState.value.lastPathPoint?.let { lastPathPoint ->
            SphericalUtil.computeDistanceBetween(lastPathPoint.toLatLng(), pathPoint.toLatLng())
        } ?: 0.0

        _runTrackingState.value = _runTrackingState.value.copy(
            avgSpeedMs = ((_runTrackingState.value.avgSpeedMs * size) + speedMs) / (size + 1),
            distanceMeters = _runTrackingState.value.distanceMeters + distance.toFloat(),
            kcalBurned = _runTrackingState.value.kcalBurned + calories.toFloat(),
            timeElapsedMillis = timeElapsedMillis,
            lastPathPoint = pathPoint,
            pathPointList = _runTrackingState.value.pathPointList.apply {
                last().add(pathPoint)
            }
        )
    }

    fun startTracking() {
        if (isGpsEnabled.value)
            _runTrackingState.value = _runTrackingState.value.copy(
                pathPointList = _runTrackingState.value.pathPointList.toMutableList().apply { add(mutableListOf()) },
                isTracking = true
            ).also {
                runTrackingTimerManager.startTimer()
            }
        else
            _runTrackingState.value = _runTrackingState.value.copy(
                isTracking = false
            )
    }

    fun pauseTracking() {
        _runTrackingState.value = _runTrackingState.value.copy(
            lastPathPoint = null,
            isTracking = false
        ).also {
            runTrackingTimerManager.pauseTimer()
        }
    }

    fun stopTracking() {
        _runTrackingState.value = CurrentRunState()
        runTrackingTimerManager.stopTimer()
    }

    fun unRegisterLocationReceiver() {
        context.unregisterReceiver(locationStateReceiver)
    }

    fun registerLocationReceiver() {
        context.registerReceiver(locationStateReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
    }

}