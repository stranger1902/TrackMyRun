package com.example.trackmyrun.run_new.presentation

import com.example.trackmyrun.service.domain.repository.CountdownManager
import com.example.trackmyrun.service.data.repository.RunTrackingManager
import com.example.trackmyrun.home.domain.repository.RunRepository
import com.example.trackmyrun.core.utils.FileImageManager
import com.example.trackmyrun.core.domain.model.RunModel
import com.example.trackmyrun.service.RunTrackingService
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.trackmyrun.core.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import android.graphics.Bitmap
import android.content.Context
import android.content.Intent
import javax.inject.Inject

@HiltViewModel
class NewRunViewModel @Inject constructor(
    private val runTrackingManager: RunTrackingManager,
    @ApplicationContext private val context: Context,
    private val fileImageManager: FileImageManager,
    private val countdownManager: CountdownManager,
    private val runRepository: RunRepository
): ViewModel() {

    val countdownIsRunning = countdownManager.isRunning
    val countdown = countdownManager.countdown

    val isGpsEnabled = runTrackingManager.isGpsEnabled

    val state = runTrackingManager.runTrackingState

    fun startRunning() {
        if (!countdownIsRunning.value && countdown.value == 0)
            skipCountdown()
        else
            countdownManager.startCountdown(
                initialValue = Constants.RUN_COUNTDOWN_INITIAL_VALUE,
                onCountdownEnd = {
                    skipCountdown()
                }
            )
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
        }.also {
            countdownManager.resetCountdown()
        }
    }

    fun skipCountdown() {
        countdownManager.skipCountdown {
            Intent(context, RunTrackingService::class.java).apply {
                action = RunTrackingService.START_RUN_TRACKING
                context.startForegroundService(this)
            }
        }
    }

    fun saveSnapshot(snapshot: Bitmap, filename: String) {
        viewModelScope.launch {
            fileImageManager.saveImage(snapshot, filename)
        }
    }

    fun saveRun(run: RunModel) {
        viewModelScope.launch {
            runRepository.insertRun(run)
        }
    }

}