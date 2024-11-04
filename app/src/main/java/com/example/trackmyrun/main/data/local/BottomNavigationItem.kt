package com.example.trackmyrun.main.data.local

import com.example.trackmyrun.main.navigation.BottomNavigationDestination
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val route: BottomNavigationDestination,
    val unSelectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val title: String
)