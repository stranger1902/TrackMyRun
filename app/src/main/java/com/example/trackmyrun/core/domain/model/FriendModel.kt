package com.example.trackmyrun.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FriendModel(
    val startTimestamp: Long,
    val name: String,
    val id: String
)