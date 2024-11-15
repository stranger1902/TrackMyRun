package com.example.trackmyrun.run_friend.presentation

import com.example.trackmyrun.bluetooth.domain.chat.BluetoothDeviceDomain
import com.example.trackmyrun.profile.domain.repository.FriendRepository
import com.example.trackmyrun.bluetooth.domain.chat.BluetoothController
import com.example.trackmyrun.bluetooth.presentation.BluetoothUiState
import com.example.trackmyrun.bluetooth.domain.chat.ConnectionResult
import com.example.trackmyrun.core.domain.model.FriendModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class RunFriendViewModel @Inject constructor(
    private val bluetoothController: BluetoothController,
    private val friendRepository: FriendRepository
    ): ViewModel() {

    private val _state = MutableStateFlow(BluetoothUiState())

    private var deviceConnectionJob: Job? = null

    val errors = bluetoothController.errors

    private val _isFriendshipRequestAccepted = Channel<FriendModel>()
    val isFriendshipRequestAccepted = _isFriendshipRequestAccepted.receiveAsFlow()

    private val _isWaitingFriendshipRequest = MutableStateFlow(false)
    val isWaitingFriendshipRequest = _isWaitingFriendshipRequest.asStateFlow()

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
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value,
        scope = viewModelScope
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

    fun waitForIncomingConnections() {

        bluetoothController.makeDiscoverable()

        _state.update { it.copy(isConnecting = true) }

        _isWaitingFriendshipRequest.update { true }

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

    fun saveFriend(friend: FriendModel) {
        viewModelScope.launch {
            friendRepository.insertFriend(friend)
        }
    }

    private fun Flow<ConnectionResult>.listen(): Job = onEach { result ->

        when(result) {

            ConnectionResult.ConnectionEstablished -> {

                _isWaitingFriendshipRequest.update { false }

                _state.update {
                    it.copy(
                        messages = emptyList(),
                        isConnecting = false,
                        isConnected = true
                    )
                }
            }

            ConnectionResult.Error -> {

                _isWaitingFriendshipRequest.update { false }

                _state.update {
                    it.copy(
                        messages = emptyList(),
                        isConnecting = false,
                        isConnected = false
                    )
                }
            }

            is ConnectionResult.TransferSucceeded ->
                _isFriendshipRequestAccepted.send(
                    Json.decodeFromString(result.message.message)
                )
        }
    }.catch { throwable ->

        throwable.printStackTrace()

        bluetoothController.closeConnection()

        _isWaitingFriendshipRequest.update { false }

        _state.update {
            it.copy(
                messages = emptyList(),
                isConnecting = false,
                isConnected = false
            )
        }
    }.launchIn(viewModelScope)

    override fun onCleared() {
        super.onCleared().also {
            bluetoothController.makeUndiscovered()
        }
    }

}