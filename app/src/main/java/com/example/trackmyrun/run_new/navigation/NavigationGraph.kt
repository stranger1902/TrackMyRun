package com.example.trackmyrun.run_new.navigation

import com.example.trackmyrun.run_detail.presentation.RunDetailScaffoldScreen
import com.example.trackmyrun.run_new.presentation.NewRunScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.compose.ui.Modifier
import androidx.navigation.toRoute

fun NavGraphBuilder.registerNewRunGraph(navController: NavHostController, onCloseRunDetail: () -> Unit) {

    navigation<NewRunGraph>(
        startDestination = NewRunDestination.NewRunScreen
    ) {

        composable<NewRunDestination.NewRunScreen> {
            NewRunScreen(
                modifier = Modifier
                    .fillMaxSize(),
                onNavigateToRunDetailScreen = { run ->
                    navController.navigate(
                        NewRunDestination.RunDetailScreen(run.id)
                    )
                }
            )
        }

        composable<NewRunDestination.RunDetailScreen> {

            val args = it.toRoute<NewRunDestination.RunDetailScreen>()

            RunDetailScaffoldScreen(
                runId = args.runId,
                onBackPressed = {
                    onCloseRunDetail()
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }

    }
}
