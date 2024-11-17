package com.example.trackmyrun.service

import com.example.trackmyrun.service.data.repository.ServiceNotificationManager
import com.example.trackmyrun.service.data.repository.RunTrackingManager
import com.example.trackmyrun.service.domain.model.TrackingInfoModel
import kotlinx.coroutines.flow.distinctUntilChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.Dispatchers
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
    private var isLaunched = false

    companion object {
        const val UNREGISTER_GPS_LISTENER = "UNREGISTER_GPS"
        const val REGISTER_GPS_LISTENER = "REGISTER_GPS"
        const val START_RUN_TRACKING = "START_TRACKING"
        const val PAUSE_RUN_TRACKING = "PAUSE_TRACKING"
        const val STOP_RUN_TRACKING = "STOP_TRACKING"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        super.onStartCommand(intent, flags, startId)

        val action = intent?.action

        when(action) {
            UNREGISTER_GPS_LISTENER -> runTrackingManager.unRegisterLocationReceiver()
            REGISTER_GPS_LISTENER -> runTrackingManager.registerLocationReceiver()
            START_RUN_TRACKING -> startTracking()
            PAUSE_RUN_TRACKING -> pauseTracking()
            STOP_RUN_TRACKING -> stopTracking()
        }

        return if (action == STOP_RUN_TRACKING) START_NOT_STICKY else START_STICKY
    }

    private fun startTracking() {

        if (!isLaunched) {
            startForeground(ServiceNotificationManager.RUN_TRACKING_NOTIFICATION_ID, notificationManager.baseNotification.build())
            isLaunched = true
        }

        if (runTrackingJob == null)
            runTrackingJob = combine(
                runTrackingManager.currentGpsLocation,
                runTrackingManager.timeElapsedMillis,
                runTrackingManager.isGpsEnabled
            ) { gpsLocation, timeElapsedMillis, isGpsEnabled ->
                TrackingInfoModel(
                    timeElapsedMillis = timeElapsedMillis,
                    isGpsEnabled = isGpsEnabled,
                    gpsLocation = gpsLocation
                )
            }
            .onStart {
                runTrackingManager.startTracking()
            }
            .distinctUntilChanged { old, new ->
                old.timeElapsedMillis == new.timeElapsedMillis
            }
            .onEach {

                if (!it.isGpsEnabled) {
                    pauseTracking()
                    return@onEach
                }

                notificationManager.updateServiceTrackingNotification(true, it.timeElapsedMillis)

                runTrackingManager.updateRunTrackingState(
                    timeElapsedMillis = it.timeElapsedMillis,
                    pathPoint = it.gpsLocation.pathPoint,
                    speedMs = it.gpsLocation.speedMs
                )
            }
            .launchIn(serviceScope)
    }

    private fun pauseTracking() {
        notificationManager.updateServiceTrackingNotification(false, null)
        runTrackingManager.pauseTracking()
        runTrackingJob?.cancel()
        runTrackingJob = null
    }

    private fun stopTracking() {

        notificationManager.updateServiceTrackingNotification(false, null)
        runTrackingManager.stopTracking()
        runTrackingJob?.cancel()

        if (isLaunched) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    override fun onDestroy() {

        super.onDestroy()

        runTrackingJob = null
        serviceScope.cancel()
        isLaunched = false
    }

}