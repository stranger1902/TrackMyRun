package com.example.trackmyrun.home.presentation

import com.example.trackmyrun.main.navigation.BottomNavigationDestination
import com.example.trackmyrun.home.navigation.registerHomeGraph
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeMainScreen(
    modifier: Modifier = Modifier,
    onNewRunClick: () -> Unit
) {

    val navController = rememberNavController()

    NavHost(
        startDestination = BottomNavigationDestination.HomeGraph,
        navController = navController,
        modifier = modifier
    ) {
        registerHomeGraph(navController = navController, onNewRunClick = onNewRunClick)
    }

}