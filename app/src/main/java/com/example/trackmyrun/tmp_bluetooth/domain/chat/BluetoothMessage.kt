package com.example.trackmyrun.tmp_bluetooth.domain.chat

data class BluetoothMessage(
    val isFromLocalUser: Boolean,
    val senderName: String,
    val message: String
)