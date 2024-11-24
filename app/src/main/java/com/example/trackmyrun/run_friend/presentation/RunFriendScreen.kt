package com.example.trackmyrun.run_friend.presentation

import com.example.trackmyrun.run_friend.presentation.screen.NewFriendScreen
import com.example.trackmyrun.run_friend.presentation.screen.WaitingScreen
import com.example.trackmyrun.core.presentation.ObserveAsEvents
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.Toast

@Composable
fun RunFriendScreen(
    modifier: Modifier = Modifier,
    onFriendshipRequestAccepted: () -> Unit
) {

    Scaffold(
        modifier = modifier
    ) { innerPadding ->

        val viewModel = hiltViewModel<RunFriendViewModel>()

        val isWaitingFriendshipRequest by viewModel.isWaitingFriendshipRequest.collectAsStateWithLifecycle()

        val state by viewModel.state.collectAsStateWithLifecycle()

        val context = LocalContext.current

        ObserveAsEvents(viewModel.isFriendshipRequestAccepted) { friend ->
            viewModel.saveFriend(friend)
            //delay(1000)
            onFriendshipRequestAccepted()
        }

        ObserveAsEvents(viewModel.errors) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        if (isWaitingFriendshipRequest)
            WaitingScreen(
                text = "In attesa di ricevere una nuova richiesta di amicizia..",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            )
        else {
            if (state.isConnecting)
                WaitingScreen(
                    text = "In attesa di invio della richiesta di amicizia..",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
            else
                NewFriendScreen(
                    scannedDevices = state.scannedDevices,
                    pairedDevices = state.pairedDevices,
                    isDiscovering = state.isDiscovering,
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
        }
    }
}