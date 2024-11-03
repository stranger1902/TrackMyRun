package com.example.trackmyrun.core.utils

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

class Constants {

    companion object {

        val HEIGHT_USER_DATASTORE_KEY = intPreferencesKey("height")
        val WEIGHT_USER_DATASTORE_KEY = intPreferencesKey("weight")
        val NAME_USER_DATASTORE_KEY = stringPreferencesKey("name")

        const val USER_DATASTORE_KEY = "user"

        const val MAX_WEIGHT_KG = 600
        const val MAX_HEIGHT_CM = 250
    }

}