package com.example.trackmyrun.profile.presentation

import com.example.trackmyrun.core.domain.model.FriendModel

data class ProfileState(
    val items: List<FriendModel> = emptyList(),
    val isEndReached: Boolean = false,
    val isLoading: Boolean = false,
    val pageSize: Int
)