package com.example.trackmyrun.run_new.presentation

import com.example.trackmyrun.service.data.repository.RunTrackingManager
import com.example.trackmyrun.service.RunTrackingService
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import javax.inject.Inject

@HiltViewModel
class NewRunViewModel @Inject constructor(
    private val runTrackingManager: RunTrackingManager,
    @ApplicationContext private val context: Context
): ViewModel() {

    val state = runTrackingManager.runTrackingState

    fun startRunning() {
        Intent(context, RunTrackingService::class.java).apply {
            action = RunTrackingService.START_RUN_TRACKING
            context.startForegroundService(this)
        }
    }

    fun pauseRunning() {
        Intent(context, RunTrackingService::class.java).apply {
            action = RunTrackingService.PAUSE_RUN_TRACKING
            context.startService(this)
        }
    }

    fun stopRunning() {
        Intent(context, RunTrackingService::class.java).apply {
            action = RunTrackingService.STOP_RUN_TRACKING
            context.startService(this)
        }
    }

}