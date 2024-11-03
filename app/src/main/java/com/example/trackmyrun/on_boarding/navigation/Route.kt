package com.example.trackmyrun.on_boarding.navigation

import kotlinx.serialization.Serializable

sealed class OnBoardingDestination {

    @Serializable
    data object OnBoardingScreen: OnBoardingDestination()
}

@Serializable
object OnBoardingGraph