package com.example.trackmyrun.tmp_bluetooth.data.chat

import com.example.trackmyrun.tmp_bluetooth.domain.chat.BluetoothDeviceDomain
import com.example.trackmyrun.tmp_bluetooth.domain.chat.BluetoothController
import com.example.trackmyrun.tmp_bluetooth.domain.chat.ConnectionResult
import com.example.trackmyrun.tmp_bluetooth.domain.chat.BluetoothMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import android.bluetooth.BluetoothServerSocket
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothAdapter
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.CoroutineScope
import android.content.pm.PackageManager
import android.bluetooth.BluetoothSocket
import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.emitAll
import android.annotation.SuppressLint
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Dispatchers
import android.content.IntentFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import android.content.Context
import java.io.IOException
import android.Manifest
import java.util.UUID

@SuppressLint("MissingPermission", "UnspecifiedRegisterReceiverFlag")
class AndroidBluetoothController(
    @ApplicationContext private val context: Context
): BluetoothController {

    companion object {
        const val SERVICE_UUID = "62eac33e-6fe4-4230-8776-5a2ac26585dc"
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())

    private val _isDiscovering = MutableStateFlow(false)
    private val _isConnected = MutableStateFlow(false)

    private val _errors = Channel<String>()

    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()

    override val isDiscovering: StateFlow<Boolean>
        get() = _isDiscovering.asStateFlow()

    override val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()

    override val errors: Flow<String>
        get() = _errors.receiveAsFlow()

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter

    private var dataTransferSerice: BluetoothDataTranferService? = null

    private val bluetoothStateReceiver = BluetoothStateReceiver { isConnected, device ->
        if (bluetoothAdapter.bondedDevices.contains(device))
            _isConnected.update { isConnected }
        else
            coroutineScope.launch {
                _errors.send("Can't connect to a no-paired device")
            }
    }

    private val foundDeviceReceiver = FoundDeviceReceiver(
        onDeviceFound = { device ->
            _scannedDevices.update { devices ->
                val newDevice = device.toBluetoothDeviceDomain()
                if (newDevice in devices) devices else devices + newDevice
            }
        },
        onDiscovery = { isDiscovering ->
            _isDiscovering.update { isDiscovering }
        }
    )

    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null

    init {

        updatePairedDevices()

        context.registerReceiver(
            bluetoothStateReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            }
        )
    }

    override fun startDiscovery() {

        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) return

        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                addAction(BluetoothDevice.ACTION_FOUND)
            }
        )

        updatePairedDevices()

        bluetoothAdapter.startDiscovery()
    }

    override fun stopDiscovery() {

        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) return

        bluetoothAdapter.cancelDiscovery()
    }

    override fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult> = flow {

        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT))
            throw SecurityException("No bluetooth connect permission granted")

        currentClientSocket = bluetoothAdapter
            .getRemoteDevice(device.address)
            .createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_UUID))

        stopDiscovery()

        currentClientSocket?.let { socket ->

            try {

                socket.connect()

                emit(ConnectionResult.ConnectionEstablished)

                updatePairedDevices()

                dataTransferSerice = BluetoothDataTranferService(socket)

                emitAll(
                    dataTransferSerice
                        ?.listenForIncomingMessages()
                        ?.map { message ->
                            ConnectionResult.TransferSucceeded(
                                message = message
                            )
                        } ?: emptyFlow()
                )
            }
            catch (e: IOException) {
                e.printStackTrace()
                socket.close()
                currentClientSocket = null
                _errors.send("Connection was interrupted")
                emit(ConnectionResult.Error("Connection was interrupted"))
            }
        }
    }
    .onCompletion {
        closeConnection()
    }
    .flowOn(Dispatchers.IO)

    override fun startBluetoothServer(): Flow<ConnectionResult> = flow {

        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT))
            throw SecurityException("No bluetooth connect permission granted")

        currentServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("chat_service", UUID.fromString(SERVICE_UUID))

        var shouldLoop = true

        while (shouldLoop) {

            try {
                currentClientSocket = currentServerSocket?.accept()
            } catch (e: IOException) {
                currentClientSocket = null
                e.printStackTrace()
                shouldLoop = false
            }

            emit(ConnectionResult.ConnectionEstablished)

            updatePairedDevices()

            currentClientSocket?.let {

                currentServerSocket?.close()

                dataTransferSerice = BluetoothDataTranferService(it)

                emitAll(
                    dataTransferSerice
                        ?.listenForIncomingMessages()
                        ?.map { message ->
                            ConnectionResult.TransferSucceeded(message)
                        } ?: emptyFlow()
                )
            }
        }
    }
    .onCompletion {
        closeConnection()
    }
    .flowOn(Dispatchers.IO)

    override suspend fun trySendMessage(message: String): BluetoothMessage? {

        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return null

        if (dataTransferSerice == null) return null

        val bluetoothMessage = BluetoothMessage(
            senderName = bluetoothAdapter.name ?: "Unknown name",
            isFromLocalUser = true,
            message = message
        )

        dataTransferSerice!!.sendMessage(bluetoothMessage.toByteArray())

        return bluetoothMessage
    }

    override fun closeConnection() {
        currentClientSocket?.close()
        currentServerSocket?.close()
        currentClientSocket = null
        currentServerSocket = null
    }

    override fun release() {
        context.unregisterReceiver(bluetoothStateReceiver)
        context.unregisterReceiver(foundDeviceReceiver)
        closeConnection()
    }

    private fun updatePairedDevices() {

        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return

        bluetoothAdapter.bondedDevices
            .map {
                it.toBluetoothDeviceDomain()
            }
            .also { devices ->
                _pairedDevices.update { devices }
            }
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

}