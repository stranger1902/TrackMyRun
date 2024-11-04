package com.example.trackmyrun.main.domain.repository

import com.example.trackmyrun.main.data.local.BottomNavigationItem

interface BottomNavigationRepository {
    fun getBottomNavigationItems(): List<BottomNavigationItem>
}