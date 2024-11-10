package com.example.trackmyrun.tmp_bluetooth.domain.chat

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow

interface BluetoothController {

    val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
    val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>

    val isDiscovering: StateFlow<Boolean>
    val isConnected: StateFlow<Boolean>

    val errors: Flow<String>

    fun startDiscovery()
    fun stopDiscovery()

    fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult>
    fun startBluetoothServer(): Flow<ConnectionResult>
    fun closeConnection()

    suspend fun trySendMessage(message: String): BluetoothMessage?

    fun release()
}