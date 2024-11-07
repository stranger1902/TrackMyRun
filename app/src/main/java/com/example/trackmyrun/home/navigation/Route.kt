package com.example.trackmyrun.home.navigation

import com.example.trackmyrun.main.navigation.BottomNavigationDestination
import kotlinx.serialization.Serializable

sealed class HomeDestination {

    @Serializable
    data class RunDetailScreen(
        val runId: String
    ): HomeDestination()

    @Serializable
    data object HomeScreen: HomeDestination()
}

typealias HomeGraph = BottomNavigationDestination.HomeGraph