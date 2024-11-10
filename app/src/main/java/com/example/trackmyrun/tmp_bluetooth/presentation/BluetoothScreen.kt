package com.example.trackmyrun.tmp_bluetooth.presentation

import com.example.trackmyrun.tmp_bluetooth.presentation.component.ObserveAsEvents
import com.example.trackmyrun.tmp_bluetooth.presentation.component.DeviceScreen
import com.example.trackmyrun.tmp_bluetooth.presentation.component.ChatScreen
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.widget.Toast

@Composable
fun BluetoothScreen(
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<BluetoothViewModel>()

    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(state.isConnected) {
        if (state.isConnected) Toast.makeText(context, "You are connected!", Toast.LENGTH_SHORT).show()
    }

    ObserveAsEvents(viewModel.errors) { error ->
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    when {

        state.isConnecting -> Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            CircularProgressIndicator()
            Text(text = "Connecting..")
        }

        state.isConnected -> {
            ChatScreen(
                state = state,
                onDisconnect = {
                    viewModel.disconnectFromDevice()
                },
                onSendMessage = { message ->
                    viewModel.sendMessage(message)
                }
            )
        }

        else -> DeviceScreen(
            state = state,
            onStartServer = {
                viewModel.waitForIncomingConnections()
            },
            onDeviceClick = { device ->
                viewModel.connectToDevice(device)
            },
            onStartScan = {
                viewModel.startScan()
            },
            onStopScan = {
                viewModel.stopScan()
            },
            modifier = modifier
        )
    }
}