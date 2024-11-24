package com.example.trackmyrun.core.bluetooth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BluetoothMessageModel(
    val isFromLocalUser: Boolean,
    val senderName: String,
    val message: String
)