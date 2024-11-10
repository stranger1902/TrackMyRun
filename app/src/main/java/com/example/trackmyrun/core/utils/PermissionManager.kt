package com.example.trackmyrun.core.utils

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.content.pm.PackageManager
import android.content.Context
import android.os.Build
import android.Manifest

class PermissionManager(
    @ApplicationContext private val context: Context
) {

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted = _permissionGranted.asStateFlow()

    private val permissions = mutableListOf(
        // Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            add(Manifest.permission.POST_NOTIFICATIONS)
    }.toList()

    fun checkPermissions() {
        _permissionGranted.value = permissions.all {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }

}