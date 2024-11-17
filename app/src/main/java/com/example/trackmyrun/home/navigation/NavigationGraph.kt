package com.example.trackmyrun.home.navigation

import com.example.trackmyrun.run_detail.presentation.RunDetailScreen
import com.example.trackmyrun.home.presentation.HomeScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.compose.ui.Modifier
import androidx.navigation.toRoute

fun NavGraphBuilder.registerHomeGraph(navController: NavHostController, onNewRunClick: () -> Unit) {

    navigation<HomeGraph>(
        startDestination = HomeDestination.HomeScreen
    ) {

        composable<HomeDestination.HomeScreen> {
            HomeScreen(
                onNavigateToRunDetailScreen = { run ->
                    navController.navigate(
                        HomeDestination.RunDetailScreen(run.id)
                    )
                },
                onFloatingButtonClick = {
                    onNewRunClick()
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        composable<HomeDestination.RunDetailScreen> {

            val args = it.toRoute<HomeDestination.RunDetailScreen>()

            RunDetailScreen(
                runId = args.runId,
                onBackPressed = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }

    }
}
