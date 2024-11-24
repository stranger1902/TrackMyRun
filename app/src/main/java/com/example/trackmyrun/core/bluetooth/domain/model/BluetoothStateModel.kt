package com.example.trackmyrun.core.bluetooth.domain.model

data class BluetoothStateModel(
    val scannedDevices: List<BluetoothDeviceModel> = emptyList(),
    val pairedDevices: List<BluetoothDeviceModel> = emptyList(),
    val messages: List<BluetoothMessageModel> = emptyList(),
    val isDiscovering: Boolean = false,
    val isConnecting: Boolean = false,
    val isConnected: Boolean = false
)