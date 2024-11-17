package com.example.trackmyrun.main.navigation

import com.example.trackmyrun.run_statistics.presentation.StatisticsScreen
import com.example.trackmyrun.run_friend.presentation.RunFriendScreen
import com.example.trackmyrun.run_new.presentation.NewRunMainScreen
import com.example.trackmyrun.profile.presentation.ProfileScreen
import com.example.trackmyrun.home.presentation.HomeMainScreen
import com.example.trackmyrun.run_new.navigation.NewRunGraph
import com.example.trackmyrun.main.presentation.MainScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.compose.ui.Modifier
import android.widget.Toast

fun NavGraphBuilder.registerMainGraph(navController: NavHostController) {

    navigation<MainGraph>(
        startDestination = MainDestination.MainScreen
    ) {

        composable<MainDestination.MainScreen> {
            MainScreen(
                onNewFriendClick = {
                    navController.navigate(MainDestination.RunFriendScreen)
                },
                onNewRunClick = {
                    navController.navigate(NewRunGraph)
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        composable<MainDestination.RunFriendScreen> {
            RunFriendScreen(
                onFriendshipRequestAccepted = {
                    navController.also {
                        it.popBackStack()
                        it.navigate(MainGraph)
                        Toast.makeText(navController.context, "Un nuovo amico è stato aggiunto alla lista", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        composable<MainDestination.NewRunGraph> {
            NewRunMainScreen(
                onCloseRunDetail = {
                    navController.navigate(MainGraph) {
                        popUpTo<MainGraph> {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }

    }
}

fun NavGraphBuilder.registerBottomNavigationGraph(onNewFriendClick: () -> Unit, onNewRunClick: () -> Unit) {

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
                onFloatingButtonClick = onNewFriendClick,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        composable<BottomNavigationDestination.HomeGraph> {
            HomeMainScreen(
                onNewRunClick = onNewRunClick,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }

}