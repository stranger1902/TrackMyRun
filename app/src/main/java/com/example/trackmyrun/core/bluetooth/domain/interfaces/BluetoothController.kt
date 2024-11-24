package com.example.trackmyrun.core.bluetooth.domain.interfaces

import com.example.trackmyrun.core.bluetooth.domain.model.BluetoothDeviceModel
import com.example.trackmyrun.core.bluetooth.domain.model.BluetoothMessageModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow

interface BluetoothController {

    val scannedDevices: StateFlow<List<BluetoothDeviceModel>>
    val pairedDevices: StateFlow<List<BluetoothDeviceModel>>

    val isDiscovering: StateFlow<Boolean>
    val isConnected: StateFlow<Boolean>

    val isBluetoothEnabled: Flow<Boolean>
    val makeDiscoverable: Flow<Boolean>
    val errors: Flow<String>

    fun startDiscovery()
    fun stopDiscovery()

    fun connectToDevice(device: BluetoothDeviceModel): Flow<ConnectionResult>
    fun startBluetoothServer(): Flow<ConnectionResult>

    fun makeUndiscovered()
    fun makeDiscoverable()

    fun closeConnection()

    suspend fun trySendMessage(message: String): BluetoothMessageModel?

    fun registerBluetoothReceivers()

    fun resetState()
    fun release()
}