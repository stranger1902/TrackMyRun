package com.example.trackmyrun.bluetooth.data.chat

import com.example.trackmyrun.bluetooth.domain.chat.BluetoothDeviceDomain
import com.example.trackmyrun.bluetooth.domain.chat.BluetoothController
import com.example.trackmyrun.bluetooth.domain.chat.ConnectionResult
import com.example.trackmyrun.bluetooth.domain.chat.BluetoothMessage
import com.example.trackmyrun.core.domain.model.FriendModel
import com.example.trackmyrun.core.utils.PermissionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.trackmyrun.core.utils.UserManager
import com.example.trackmyrun.core.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import android.bluetooth.BluetoothServerSocket
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.serialization.encodeToString
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothAdapter
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.CoroutineScope
import android.bluetooth.BluetoothSocket
import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onStart
import android.annotation.SuppressLint
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Dispatchers
import android.content.IntentFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.content.Context
import kotlinx.coroutines.Job
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission", "UnspecifiedRegisterReceiverFlag")
class AndroidBluetoothController(
    @ApplicationContext private val context: Context,
    private val permissionManager: PermissionManager,
    private val userManager: UserManager
): BluetoothController {

    companion object {
        const val SERVICE_UUID = "62eac33e-6fe4-4230-8776-5a2ac26585dc"
    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())

    private val _isDiscovering = MutableStateFlow(false)
    private val _isConnected = MutableStateFlow(false)

    private val _isBluetoothEnabled = Channel<Boolean>()
    private val _makeDiscoverable = Channel<Boolean>()
    private val _errors = Channel<String>()

    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()

    override val isDiscovering: StateFlow<Boolean>
        get() = _isDiscovering.asStateFlow()

    override val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()

    override val isBluetoothEnabled: Flow<Boolean>
        get() = _isBluetoothEnabled.receiveAsFlow()

    override val makeDiscoverable: Flow<Boolean>
        get() = _makeDiscoverable.receiveAsFlow()

    override val errors: Flow<String>
        get() = _errors.receiveAsFlow()

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter

    private var dataTransferSerice: BluetoothDataTranferService? = null

    private var makeDiscoverableJob: Job? = null

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
                if (bluetoothAdapter.bondedDevices.contains(device))
                    devices else devices + device.toBluetoothDeviceDomain()
            }
        },
        onDiscovery = { isDiscovering ->
            _isDiscovering.update { isDiscovering }
        }
    )

    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null

    init {
        coroutineScope.launch {
            _isBluetoothEnabled.send(bluetoothAdapter.isEnabled)
        }
    }

    override fun startDiscovery() {

        if (!permissionManager.checkBluetoothPermission()) return

        coroutineScope.launch {
            _isBluetoothEnabled.send(bluetoothAdapter.isEnabled)
        }

        if (bluetoothAdapter.isEnabled) {
            updatePairedDevices()
            bluetoothAdapter.startDiscovery()
        }
    }

    override fun stopDiscovery() {

        if (!permissionManager.checkBluetoothPermission()) return

        coroutineScope.launch {
            _isBluetoothEnabled.send(bluetoothAdapter.isEnabled)
        }

        if (bluetoothAdapter.isEnabled)
            bluetoothAdapter.cancelDiscovery()
    }

    override fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult> = flow {

        if (!permissionManager.checkBluetoothPermission())
            throw SecurityException("No bluetooth connect permission granted")

        coroutineScope.launch {
            _isBluetoothEnabled.send(bluetoothAdapter.isEnabled)
        }

        if (!bluetoothAdapter.isEnabled) return@flow

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

                val friend = FriendModel(
                    startTimestamp = System.currentTimeMillis(),
                    name = userManager.currentUser.value.name,
                    id = userManager.currentUser.value.id!!
                )

                emitAll(
                    dataTransferSerice
                        ?.listenForIncomingMessages()
                        ?.onStart {
                            // TODO: per mantenere il modulo BLuetooth indipendente, l'invio del messaggio non può essere fatto qui...
                            trySendMessage(Json.encodeToString(friend))
                        }
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
                emit(ConnectionResult.Error)
            }
        }
    }
    .onCompletion {
        closeConnection()
    }
    .flowOn(Dispatchers.IO)

    override fun startBluetoothServer(): Flow<ConnectionResult> = flow {

        if (!permissionManager.checkBluetoothPermission())
            throw SecurityException("No bluetooth connect permission granted")

        coroutineScope.launch {
            _isBluetoothEnabled.send(bluetoothAdapter.isEnabled)
        }

        if (!bluetoothAdapter.isEnabled) return@flow

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

                val friend = FriendModel(
                    startTimestamp = System.currentTimeMillis(),
                    name = userManager.currentUser.value.name,
                    id = userManager.currentUser.value.id!!
                )

                emitAll(
                    dataTransferSerice
                        ?.listenForIncomingMessages()
                        ?.onStart {
                            // TODO: per mantenere il modulo BLuetooth indipendente, l'invio del messaggio non può essere fatto qui...
                            trySendMessage(Json.encodeToString(friend))
                        }
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

    override fun makeDiscoverable() {
        makeDiscoverableJob = coroutineScope.launch {
            while (isActive) {
                _makeDiscoverable.send(true)
                delay((Constants.BLUETOOTH_DISCOVERABLE_INTERVAL_SEC) * 1000)
            }
        }
    }

    override fun makeUndiscovered() {
        coroutineScope.launch {
            _makeDiscoverable.send(false)
            makeDiscoverableJob?.cancel()
            makeDiscoverableJob = null
        }
    }

    override suspend fun trySendMessage(message: String): BluetoothMessage? {

        if (!permissionManager.checkBluetoothPermission()) return null

        if (dataTransferSerice == null) return null

        coroutineScope.launch {
            _isBluetoothEnabled.send(bluetoothAdapter.isEnabled)
        }

        if (!bluetoothAdapter.isEnabled) return null

        val bluetoothMessage = BluetoothMessage(
            senderName = bluetoothAdapter.name ?: "(No name)",
            isFromLocalUser = true,
            message = message
        )

        dataTransferSerice!!.sendMessage(bluetoothMessage.toByteArray())

        return bluetoothMessage
    }

    override fun closeConnection() {

        makeDiscoverableJob?.cancel()
        currentClientSocket?.close()
        currentServerSocket?.close()

        makeDiscoverableJob = null
        currentClientSocket = null
        currentServerSocket = null
    }

    override fun registerBluetoothReceivers() {

        context.registerReceiver(
            bluetoothStateReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            }
        )

        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                addAction(BluetoothDevice.ACTION_FOUND)
            }
        )
    }

    override fun release() {
        context.unregisterReceiver(bluetoothStateReceiver)
        context.unregisterReceiver(foundDeviceReceiver)
        closeConnection()
    }

    private fun updatePairedDevices() {

        if (!permissionManager.checkBluetoothPermission()) return

        bluetoothAdapter.bondedDevices
            .map {
                it.toBluetoothDeviceDomain()
            }
            .also { devices ->
                _pairedDevices.update { devices }
            }
    }

}