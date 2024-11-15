package com.example.trackmyrun.bluetooth.domain.chat

sealed interface ConnectionResult {

    data class TransferSucceeded(val message: BluetoothMessage): ConnectionResult

    data object ConnectionEstablished: ConnectionResult

    data object Error: ConnectionResult
}