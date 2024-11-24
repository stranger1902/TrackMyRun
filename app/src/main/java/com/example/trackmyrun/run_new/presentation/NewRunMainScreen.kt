package com.example.trackmyrun.run_new.presentation

import com.example.trackmyrun.run_new.navigation.registerNewRunGraph
import com.example.trackmyrun.run_new.navigation.NewRunGraph
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NewRunMainScreen(
    modifier: Modifier = Modifier,
    onCloseRunDetail: () -> Unit
) {

    val navController = rememberNavController()

    NavHost(
        startDestination = NewRunGraph,
        navController = navController,
        modifier = modifier
    ) {
        registerNewRunGraph(navController, onCloseRunDetail)
    }
}