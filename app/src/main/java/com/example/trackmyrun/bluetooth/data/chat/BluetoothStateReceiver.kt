package com.example.trackmyrun.bluetooth.data.chat

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BluetoothStateReceiver(
    private val onStateChanged: (isConnected: Boolean, device: BluetoothDevice) -> Unit
): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
        else
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

        when(intent?.action) {

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> onStateChanged(false, device ?: return)

            BluetoothDevice.ACTION_ACL_CONNECTED -> onStateChanged(true, device ?: return)

            BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED -> {

            }
        }
    }
}





