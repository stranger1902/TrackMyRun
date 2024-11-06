package com.example.trackmyrun.core.utils

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

class Constants {

    companion object {

        val HEIGHT_USER_DATASTORE_KEY = intPreferencesKey("height")
        val WEIGHT_USER_DATASTORE_KEY = intPreferencesKey("weight")
        val NAME_USER_DATASTORE_KEY = stringPreferencesKey("name")

        const val RUN_TRACKING_DB_NAME = "run_tracking.db"
        const val USER_DATASTORE_KEY = "user"
        const val IMAGE_FILE_PATH = "image"

        const val GPS_FASTEST_INTERVAL_MILLIS = 1000L
        const val GPS_INTERVAL_MILLIS = 3000L

        const val MAX_WEIGHT_KG = 600
        const val MAX_HEIGHT_CM = 250

        const val MAPS_SNAPSHOT_QUALITY = 80
        const val POLYLINE_WIDTH = 32f
        const val MAPS_ZOOM = 17f
    }

}