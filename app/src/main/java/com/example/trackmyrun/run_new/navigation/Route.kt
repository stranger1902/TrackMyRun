package com.example.trackmyrun.run_new.navigation

import com.example.trackmyrun.main.navigation.MainDestination
import kotlinx.serialization.Serializable

sealed class NewRunDestination {

    @Serializable
    data class RunDetailScreen(
        val runId: String
    ) : NewRunDestination()

    @Serializable
    data object NewRunScreen: NewRunDestination()
}

typealias NewRunGraph = MainDestination.NewRunGraph