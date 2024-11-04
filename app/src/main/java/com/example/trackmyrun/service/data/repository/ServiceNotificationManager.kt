package com.example.trackmyrun.service.data.repository

import com.example.trackmyrun.core.extensions.toStopwatchFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.core.app.NotificationCompat
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.trackmyrun.R
import android.content.Context
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

    val baseNotification: NotificationCompat.Builder
        get() = NotificationCompat.Builder(context, RUN_TRACKING_NOTIFICATION_CHANNEL_ID)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentText(0L.toStopwatchFormat())
            //.setContentIntent(intentToRunScreen)
            .setSmallIcon(R.drawable.ic_launcher)
            .setWhen(System.currentTimeMillis())
            .setContentTitle("Running Time")
            .setAutoCancel(false)
            .setShowWhen(true)
            .setOngoing(true)

    fun updateServiceTrackingNotification(durationInMillis: Long) {
        baseNotification
            .setContentText(durationInMillis.toStopwatchFormat())
            .setWhen(System.currentTimeMillis())
            .clearActions()
            //.addAction(getTrackingNotificationAction(isTracking))
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