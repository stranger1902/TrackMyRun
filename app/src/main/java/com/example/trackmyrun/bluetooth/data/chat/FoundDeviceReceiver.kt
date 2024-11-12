package com.example.trackmyrun.bluetooth.data.chat

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class FoundDeviceReceiver(
    private val onDeviceFound: (device: BluetoothDevice) -> Unit,
    private val onDiscovery: (isDiscovering: Boolean) -> Unit
): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
        else
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

        when(intent?.action) {

            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                onDiscovery(false)
            }

            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                onDiscovery(true)
            }

            BluetoothDevice.ACTION_FOUND -> {
                device?.let {
                    onDeviceFound(it)
                }
            }
        }
    }
}





