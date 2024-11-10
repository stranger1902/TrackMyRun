package com.example.trackmyrun.tmp_bluetooth.data.chat

import com.example.trackmyrun.tmp_bluetooth.domain.chat.BluetoothDeviceDomain
import com.example.trackmyrun.tmp_bluetooth.domain.chat.BluetoothMessage
import android.bluetooth.BluetoothDevice
import android.annotation.SuppressLint

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain = BluetoothDeviceDomain(
    address = address,
    name = name
)

fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage {

    val senderName = substringBeforeLast("#")
    val message = substringAfter("#")

    return BluetoothMessage(
        isFromLocalUser = isFromLocalUser,
        senderName = senderName,
        message = message
    )
}

fun BluetoothMessage.toByteArray(): ByteArray = "$senderName#$message".encodeToByteArray()