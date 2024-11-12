package com.example.trackmyrun.bluetooth.domain.chat

data class BluetoothMessage(
    val isFromLocalUser: Boolean,
    val senderName: String,
    val message: String
)