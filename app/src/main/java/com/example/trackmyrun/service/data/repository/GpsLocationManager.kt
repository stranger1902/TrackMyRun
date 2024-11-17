package com.example.trackmyrun.service.data.repository

import com.example.trackmyrun.service.domain.model.GpsLocationModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.example.trackmyrun.core.domain.model.PathPointModel
import com.google.android.gms.location.LocationAvailability
import dagger.hilt.android.qualifiers.ApplicationContext
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.example.trackmyrun.core.utils.Constants
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import android.annotation.SuppressLint
import kotlinx.coroutines.launch
import android.content.Context
import javax.inject.Inject
import android.os.Looper

class GpsLocationManager @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) {

    private val locationRequest = LocationRequest.Builder(Constants.GPS_INTERVAL_MILLIS)
        .setMinUpdateIntervalMillis(Constants.GPS_FASTEST_INTERVAL_MILLIS)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    @SuppressLint("MissingPermission")
    val currentGpsLocation = callbackFlow {

        val locationCallback = object: LocationCallback() {

            override fun onLocationResult(result: LocationResult) {

                super.onLocationResult(result)

                result.locations.forEach { location ->
                    launch {
                        send(
                            GpsLocationModel(
                                pathPoint = PathPointModel(
                                    longitude = location.longitude,
                                    latitude = location.latitude
                                ),
                                speedMs = location.speed
                            )
                        )
                    }
                }
            }
        }

        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        awaitClose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

}