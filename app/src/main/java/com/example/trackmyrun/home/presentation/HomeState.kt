package com.example.trackmyrun.home.presentation

import com.example.trackmyrun.core.domain.model.RunModel

data class HomeState(
    val items: List<RunModel> = emptyList(),
    val isEndReached: Boolean = false,
    val isLoading: Boolean = false,
    val pageSize: Int
)