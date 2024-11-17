package com.example.trackmyrun.service

import android.content.BroadcastReceiver
import android.location.LocationManager
import android.content.Context
import android.content.Intent

class LocationStateReceiver(
    private val onStateChanged: (isGpsEnabled: Boolean) -> Unit
): BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        when(intent?.action) {
            LocationManager.PROVIDERS_CHANGED_ACTION ->
                onStateChanged(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        }
    }
}