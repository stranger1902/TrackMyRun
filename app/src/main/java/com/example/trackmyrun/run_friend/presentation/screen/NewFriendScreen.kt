package com.example.trackmyrun.run_friend.presentation.screen

import com.example.trackmyrun.run_friend.presentation.component.BluetoothDeviceList
import com.example.trackmyrun.core.bluetooth.domain.model.BluetoothDeviceModel
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewFriendScreen(
    modifier: Modifier = Modifier,
    scannedDevices: List<BluetoothDeviceModel>,
    pairedDevices: List<BluetoothDeviceModel>,
    isDiscovering: Boolean,
    onDeviceClick: (device: BluetoothDeviceModel) -> Unit,
    onStartServer: () -> Unit,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sei pronto a fare nuove amicizie?",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )

        Card {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(16.dp)
            ) {

                Text(
                    text = "Premi il bottone qui sotto per inviare una richiesta di amicizia ad un altro runner nelle vicinanze",
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = {
                        if (!isDiscovering) onStartScan() else onStopScan()
                    }
                ) { Text(text = if (isDiscovering) "Ferma ricerca..." else "Avvia ricerca amici") }

                Text(
                    text = "Premi il bottone qui sotto per permettere ad un altro runner di diventare tuo amico",
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = {
                        onStartServer()
                    }
                ) { Text(text =  "Avvia") }
            }
        }

        BluetoothDeviceList(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            onClick = { device ->
                onDeviceClick(device)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}