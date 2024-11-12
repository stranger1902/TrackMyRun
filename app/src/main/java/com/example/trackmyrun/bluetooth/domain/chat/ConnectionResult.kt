package com.example.trackmyrun.bluetooth.domain.chat

sealed interface ConnectionResult {

    data class TransferSucceeded(val message: BluetoothMessage): ConnectionResult

    data class Error(val message: String): ConnectionResult

    data object ConnectionEstablished: ConnectionResult
}