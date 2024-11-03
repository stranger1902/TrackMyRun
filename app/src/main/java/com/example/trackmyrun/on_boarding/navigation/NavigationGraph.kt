package com.example.trackmyrun.on_boarding.navigation

import com.example.trackmyrun.on_boarding.presentation.OnBoardingScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.compose.ui.Modifier

fun NavGraphBuilder.registerOnBoardingGraph(
    onBoardingCompleted: () -> Unit
) {

    navigation<OnBoardingGraph>(
        startDestination = OnBoardingDestination.OnBoardingScreen
    ) {

        composable<OnBoardingDestination.OnBoardingScreen> {
            OnBoardingScreen(
                modifier = Modifier
                    .fillMaxSize(),
                onBoardingCompleted = {
                    onBoardingCompleted()
                }
            )
        }
    }
}