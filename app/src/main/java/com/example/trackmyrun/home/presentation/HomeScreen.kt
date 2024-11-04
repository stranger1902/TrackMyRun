package com.example.trackmyrun.home.presentation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<HomeViewModel>()

    val user = viewModel.currentUser.collectAsStateWithLifecycle()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(text = "HOME SCREEN - ${user.value.name}")
    }
}