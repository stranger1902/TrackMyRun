package com.example.trackmyrun.main.navigation

import kotlinx.serialization.Serializable

sealed class MainDestination {

    @Serializable
    data object RunFriendScreen: MainDestination()

    @Serializable
    data object NewRunGraph: MainDestination()

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
    data object HomeGraph: BottomNavigationDestination()
}

@Serializable
object BottomNavigationGraph