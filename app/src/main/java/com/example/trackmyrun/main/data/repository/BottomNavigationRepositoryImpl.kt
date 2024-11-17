package com.example.trackmyrun.main.data.repository

import com.example.trackmyrun.main.domain.repository.BottomNavigationRepository
import com.example.trackmyrun.main.navigation.BottomNavigationDestination
import com.example.trackmyrun.main.data.local.BottomNavigationItem
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.Icons

class BottomNavigationRepositoryImpl: BottomNavigationRepository {

    override fun getBottomNavigationItems(): List<BottomNavigationItem> = listOf(

        BottomNavigationItem(
            route = BottomNavigationDestination.HomeGraph,
            unSelectedIcon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home,
            title = "Home"
        ),

        BottomNavigationItem(
            route = BottomNavigationDestination.StatisticsScreen,
            unSelectedIcon = Icons.Outlined.Info,
            selectedIcon = Icons.Filled.Info,
            title = "Statistiche"
        ),

        BottomNavigationItem(
            route = BottomNavigationDestination.ProfileScreen,
            unSelectedIcon = Icons.Outlined.Person,
            selectedIcon = Icons.Filled.Person,
            title = "Profilo"
        )
    )
}