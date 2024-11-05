package com.example.trackmyrun.service.data.repository

import com.example.trackmyrun.service.domain.model.GpsLocationModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.example.trackmyrun.core.domain.model.PathPointModel
import dagger.hilt.android.qualifiers.ApplicationContext
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import android.location.LocationManager
import android.annotation.SuppressLint
import kotlinx.coroutines.launch
import android.content.Context
import javax.inject.Inject
import android.os.Looper

class GpsLocationManager @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val FASTEST_INTERVAL_MILLIS = 3000L
        private const val INTERVAL_MILLIS = 5000L
    }

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    val currentGpsLocation = callbackFlow {

        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

//        if (!isNetworkEnabled && !isGpsEnabled) throw RuntimeException("Enable GPS or Network")

        val request = LocationRequest.Builder(INTERVAL_MILLIS)
            .setMinUpdateIntervalMillis(FASTEST_INTERVAL_MILLIS)
            .build()

        val locationCallback = object : LocationCallback() {
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

        locationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

        awaitClose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

}