package com.example.trackmyrun.bluetooth.domain.chat

import kotlinx.serialization.Serializable

@Serializable
data class BluetoothMessage(
    val isFromLocalUser: Boolean,
    val senderName: String,
    val message: String
)