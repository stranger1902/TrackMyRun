package com.example.trackmyrun.bluetooth.presentation

import com.example.trackmyrun.bluetooth.domain.chat.BluetoothDeviceDomain
import com.example.trackmyrun.bluetooth.domain.chat.BluetoothMessage

data class BluetoothUiState(
    val scannedDevices: List<BluetoothDeviceDomain> = emptyList(),
    val pairedDevices: List<BluetoothDeviceDomain> = emptyList(),
    val messages: List<BluetoothMessage> = emptyList(),
    val isDiscovering: Boolean = false,
    val isConnecting: Boolean = false,
    val isConnected: Boolean = false
)