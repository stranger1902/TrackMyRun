package com.example.trackmyrun.main.navigation

import com.example.trackmyrun.statistics.presentation.StatisticsScreen
import com.example.trackmyrun.run_detail.presentation.RunDetailScreen
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
import androidx.navigation.toRoute

fun NavGraphBuilder.registerMainGraph(navController: NavHostController) {

    navigation<MainGraph>(
        startDestination = MainDestination.MainScreen
    ) {

        composable<MainDestination.MainScreen> {
            MainScreen(
                mainGraphNavController = navController,
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
                    .fillMaxSize(),
                onNavigateToRunDetailScreen = { run ->
                    navController.navigate(
                        MainDestination.RunDetailScreen(
                            isFromNewRun = true,
                            runId = run.id
                        )
                    ) {
                        popUpTo<MainGraph> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<MainDestination.RunDetailScreen> {

            val args = it.toRoute<MainDestination.RunDetailScreen>()

            RunDetailScreen(
                runId = args.runId,
                onBackPressed = {
                    if (args.isFromNewRun)
                        navController.navigate(MainGraph)
                    else
                        navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }

    }
}

fun NavGraphBuilder.registerBottomNavigationGraph(mainGraphNavController: NavHostController) {

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
                onNavigateToRunDetailScreen = { run ->
                    mainGraphNavController.navigate(
                        MainDestination.RunDetailScreen(
                            isFromNewRun = false,
                            runId = run.id
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }

}