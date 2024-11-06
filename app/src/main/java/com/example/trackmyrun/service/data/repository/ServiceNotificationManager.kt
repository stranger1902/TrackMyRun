package com.example.trackmyrun.service.data.repository

import com.example.trackmyrun.core.extensions.toStopwatchFormat
import com.example.trackmyrun.service.RunTrackingService
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.core.app.NotificationCompat
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import com.example.trackmyrun.R
import android.content.Context
import android.content.Intent
import javax.inject.Inject
import android.os.Build

class ServiceNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
){

    companion object {
        private const val RUN_TRACKING_NOTIFICATION_CHANNEL_ID = "run_tracking_channel"
        private const val RUN_TRACKING_NOTIFICATION_CHANNEL_NAME = "Run Tracking"
        const val RUN_TRACKING_NOTIFICATION_ID = 999
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var durationInMillis = 0L
    private var isTracking = false

    val baseNotification: NotificationCompat.Builder
        get() = NotificationCompat.Builder(context, RUN_TRACKING_NOTIFICATION_CHANNEL_ID)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentText(durationInMillis.toStopwatchFormat())
            .addAction(runTrackingNotificationAction)
            //.setContentIntent(intentToRunScreen)
            .setSmallIcon(R.drawable.ic_launcher)
            .setWhen(System.currentTimeMillis())
            .setContentTitle("Running Time")
            .setAutoCancel(false)
            .setShowWhen(true)
            .setOngoing(true)

    private val runTrackingNotificationAction: NotificationCompat.Action
        get() = NotificationCompat.Action(
            R.drawable.ic_stopwatch,
            if (isTracking) "Pause" else "Start",
            PendingIntent.getService(
                context,
                2234,
                Intent(
                    context,
                    RunTrackingService::class.java
                ).apply {
                    action = if (isTracking) RunTrackingService.PAUSE_RUN_TRACKING else RunTrackingService.START_RUN_TRACKING
                },
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

    fun updateServiceTrackingNotification(isTracking: Boolean, durationInMillis: Long?) {
        baseNotification
            .setWhen(System.currentTimeMillis())
            .apply {

                if (durationInMillis != null) {
                    this@ServiceNotificationManager.durationInMillis = durationInMillis
                    setContentText(durationInMillis.toStopwatchFormat())
                } else
                    setContentText(this@ServiceNotificationManager.durationInMillis.toStopwatchFormat())

                if (this@ServiceNotificationManager.isTracking != isTracking) {
                    this@ServiceNotificationManager.isTracking = isTracking
                    clearActions()
                    addAction(runTrackingNotificationAction)
                }
            }
            .also {
                notificationManager.notify(RUN_TRACKING_NOTIFICATION_ID, it.build())
            }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(RUN_TRACKING_NOTIFICATION_CHANNEL_ID, RUN_TRACKING_NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW).also {
                notificationManager.createNotificationChannel(it)
            }
        }
    }

}