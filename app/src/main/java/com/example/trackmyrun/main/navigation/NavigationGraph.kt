package com.example.trackmyrun.main.navigation

import com.example.trackmyrun.statistics.presentation.StatisticsScreen
import com.example.trackmyrun.profile.presentation.ProfileScreen
import com.example.trackmyrun.run_new.presentation.NewRunScreen
import com.example.trackmyrun.main.presentation.MainScreen
import com.example.trackmyrun.home.presentation.HomeScreen
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

fun NavGraphBuilder.registerBottomNavigationGraph() {

    navigation<BottomNavigationGraph>(
        startDestination = BottomNavigationDestination.HomeScreen
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

        composable<BottomNavigationDestination.HomeScreen> {
            HomeScreen(
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}