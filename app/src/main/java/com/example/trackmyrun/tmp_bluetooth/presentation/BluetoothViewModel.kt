package com.example.trackmyrun.tmp_bluetooth.presentation

import com.example.trackmyrun.tmp_bluetooth.domain.chat.BluetoothDeviceDomain
import com.example.trackmyrun.tmp_bluetooth.domain.chat.BluetoothController
import com.example.trackmyrun.tmp_bluetooth.domain.chat.ConnectionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
): ViewModel() {

    private val _state = MutableStateFlow(BluetoothUiState())

    private var deviceConnectionJob: Job? = null

    val errors = bluetoothController.errors

    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            messages = if (state.isConnected) state.messages else emptyList(),
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _state.value
    )

    init {

        bluetoothController.isDiscovering.onEach { isDiscovering ->
            _state.update { it.copy(isDiscovering = isDiscovering) }
        }.launchIn(viewModelScope)

        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)
    }

    fun connectToDevice(device: BluetoothDeviceDomain) {

        _state.update { it.copy(isConnecting = true) }

        deviceConnectionJob = bluetoothController
            .connectToDevice(device)
            .listen()
    }

    fun disconnectFromDevice() {

        deviceConnectionJob?.cancel()

        bluetoothController.closeConnection()

        _state.update { it.copy(isConnecting = false, isConnected = false) }
    }

    fun waitForIncomingConnections() {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .startBluetoothServer()
            .listen()
    }

    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val bluetoothMessage = bluetoothController.trySendMessage(message)
            if (bluetoothMessage != null)
                _state.update { it.copy(messages = it.messages + bluetoothMessage) }
        }
    }

    private fun Flow<ConnectionResult>.listen(): Job = onEach { result ->

        when(result) {

            ConnectionResult.ConnectionEstablished ->
                _state.update {
                    it.copy(
                        isConnecting = false,
                        isConnected = true
                    )
                }

            is ConnectionResult.Error ->
                _state.update {
                    it.copy(
                        isConnecting = false,
                        isConnected = false
                    )
                }

            is ConnectionResult.TransferSucceeded ->
                _state.update { it.copy(messages = it.messages + result.message) }
        }
    }.catch { throwable ->
        throwable.printStackTrace()
        bluetoothController.closeConnection()
        _state.update {
            it.copy(
                isConnecting = false,
                isConnected = false
            )
        }
    }.launchIn(viewModelScope)

    override fun onCleared() {
        super.onCleared().also {
            bluetoothController.release()
        }
    }

}