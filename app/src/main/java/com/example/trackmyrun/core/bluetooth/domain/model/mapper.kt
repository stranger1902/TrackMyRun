package com.example.trackmyrun.core.bluetooth.domain.model

import kotlinx.serialization.encodeToString
import android.bluetooth.BluetoothDevice
import android.annotation.SuppressLint
import kotlinx.serialization.json.Json

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceModel = BluetoothDeviceModel(
    address = address,
    name = name
)

fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessageModel {
    return Json.decodeFromString<BluetoothMessageModel>(this)
        .copy(isFromLocalUser = isFromLocalUser)
}

fun BluetoothMessageModel.toByteArray(): ByteArray = Json.encodeToString(this).encodeToByteArray()