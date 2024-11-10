package com.example.trackmyrun.tmp_bluetooth.domain.chat

typealias BluetoothDeviceDomain = BluetoothDevice

data class BluetoothDevice(
    val address: String,         // MAC address
    val name: String?
)