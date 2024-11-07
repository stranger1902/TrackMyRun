package com.example.trackmyrun.main.navigation

import com.example.trackmyrun.statistics.presentation.StatisticsScreen
import com.example.trackmyrun.profile.presentation.ProfileScreen
import com.example.trackmyrun.run_new.presentation.NewRunScreen
import com.example.trackmyrun.home.navigation.registerHomeGraph
import com.example.trackmyrun.main.presentation.MainScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.compose.ui.Modifier

fun NavGraphBuilder.registerMainGraph(navController: NavHostController) {

    navigation<MainGraph>(
        startDestination = MainDestination.MainScreen
    ) {

        composable<MainDestination.MainScreen> {
            MainScreen(
                modifier = Modifier
                    .fillMaxSize(),
                onFloatingButtonClick = {
                    navController.navigate(MainDestination.NewRunScreen)
                }
            )
        }

        composable<MainDestination.NewRunScreen> {
            NewRunScreen(
                modifier = Modifier
                    .fillMaxSize()
            )
        }

    }
}

fun NavGraphBuilder.registerBottomNavigationGraph(navController: NavHostController) {

    navigation<BottomNavigationGraph>(
        startDestination = BottomNavigationDestination.HomeGraph
    ) {

        composable<BottomNavigationDestination.StatisticsScreen> {
            StatisticsScreen(
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        composable<BottomNavigationDestination.ProfileScreen> {
            ProfileScreen(
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        registerHomeGraph(navController)
    }
}