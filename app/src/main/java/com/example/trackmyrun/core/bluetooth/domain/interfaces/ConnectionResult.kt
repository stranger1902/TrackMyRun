package com.example.trackmyrun.core.bluetooth.domain.interfaces

import com.example.trackmyrun.core.bluetooth.domain.model.BluetoothMessageModel

sealed interface ConnectionResult {

    data class TransferSucceeded(val message: BluetoothMessageModel): ConnectionResult

    data object ConnectionEstablished: ConnectionResult

    data object Error: ConnectionResult
}