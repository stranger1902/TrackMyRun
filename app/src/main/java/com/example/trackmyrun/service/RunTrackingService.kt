package com.example.trackmyrun.service

import com.example.trackmyrun.service.data.repository.ServiceNotificationManager
import com.example.trackmyrun.service.data.repository.RunTrackingManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.cancel
import kotlinx.coroutines.Job
import android.content.Intent
import javax.inject.Inject
import android.app.Service
import android.os.IBinder

@AndroidEntryPoint
class RunTrackingService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var notificationManager: ServiceNotificationManager

    @Inject
    lateinit var runTrackingManager: RunTrackingManager

    private var runTrackingJob: Job? = null

    companion object {
        const val START_RUN_TRACKING = "START_TRACKING"
        const val PAUSE_RUN_TRACKING = "PAUSE_TRACKING"
        const val STOP_RUN_TRACKING = "STOP_TRACKING"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        super.onStartCommand(intent, flags, startId)

        val action = intent?.action

        when(action) {
            START_RUN_TRACKING -> startTracking()
            PAUSE_RUN_TRACKING -> pauseTracking()
            STOP_RUN_TRACKING -> stopTracking()
        }

        return if (action == STOP_RUN_TRACKING) START_NOT_STICKY else START_STICKY
    }

    private fun startTracking() {

        startForeground(ServiceNotificationManager.RUN_TRACKING_NOTIFICATION_ID, notificationManager.baseNotification.build())

        if (runTrackingJob == null)
            runTrackingJob = combine(
                runTrackingManager.currentGpsLocation,
                flow {
                    repeat(100) { emit(it) }
                }
            ) { currentGpsLocation, _ ->
                currentGpsLocation
            }
            .onStart {
                runTrackingManager.startTracking()
            }
            .onEach {
                println(it)
            }
            .launchIn(serviceScope)
    }

    private fun pauseTracking() {
        runTrackingManager.pauseTracking()
        runTrackingJob?.cancel()
    }

    private fun stopTracking() {
        runTrackingManager.stopTracking()
        runTrackingJob?.cancel()
        runTrackingJob = null
    }

    override fun onDestroy() {

        super.onDestroy()

        runTrackingJob = null
        serviceScope.cancel()

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

}