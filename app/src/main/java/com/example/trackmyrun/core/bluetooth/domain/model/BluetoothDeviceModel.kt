package com.example.trackmyrun.core.bluetooth.domain.model

data class BluetoothDeviceModel(
    val address: String,         // MAC address
    val name: String?
)