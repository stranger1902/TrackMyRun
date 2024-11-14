package com.example.trackmyrun.bluetooth.data.chat

import com.example.trackmyrun.bluetooth.domain.chat.BluetoothDeviceDomain
import com.example.trackmyrun.bluetooth.domain.chat.BluetoothMessage
import kotlinx.serialization.encodeToString
import android.bluetooth.BluetoothDevice
import android.annotation.SuppressLint
import kotlinx.serialization.json.Json

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain = BluetoothDeviceDomain(
    address = address,
    name = name
)

fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage {
    return Json.decodeFromString<BluetoothMessage>(this)
        .copy(isFromLocalUser = isFromLocalUser)
}

fun BluetoothMessage.toByteArray(): ByteArray = Json.encodeToString(this).encodeToByteArray()