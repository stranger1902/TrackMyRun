package com.example.trackmyrun.bluetooth.domain.chat

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow

interface BluetoothController {

    val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
    val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>

    val isBluetoothEnabled: StateFlow<Boolean>
    val isDiscovering: StateFlow<Boolean>
    val isConnected: StateFlow<Boolean>

    val makeDiscoverable: Flow<Boolean>
    val errors: Flow<String>

    fun startDiscovery()
    fun stopDiscovery()

    fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult>
    fun startBluetoothServer(): Flow<ConnectionResult>
    fun makeDiscoverable()
    fun closeConnection()

    suspend fun trySendMessage(message: String): BluetoothMessage?

    fun registerBluetoothReceivers()
    fun release()
}