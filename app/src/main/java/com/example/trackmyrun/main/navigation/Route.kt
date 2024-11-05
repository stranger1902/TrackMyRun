package com.example.trackmyrun.main.navigation

import kotlinx.serialization.Serializable

sealed class MainDestination {

    @Serializable
    data object NewRunScreen: MainDestination()

    @Serializable
    data object MainScreen: MainDestination()
}

@Serializable
object MainGraph

sealed class BottomNavigationDestination {

    @Serializable
    data object StatisticsScreen: BottomNavigationDestination()

    @Serializable
    data object ProfileScreen: BottomNavigationDestination()

    @Serializable
    data object HomeScreen: BottomNavigationDestination()
}

@Serializable
object BottomNavigationGraph
