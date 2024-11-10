package com.example.trackmyrun.tmp_bluetooth.presentation.component

import com.example.trackmyrun.tmp_bluetooth.domain.chat.BluetoothDeviceDomain
import com.example.trackmyrun.tmp_bluetooth.presentation.BluetoothUiState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeviceScreen(
    modifier: Modifier = Modifier,
    state: BluetoothUiState,
    onDeviceClick: (BluetoothDeviceDomain) -> Unit,
    onStartServer: () -> Unit,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit
) {

    Column(
        modifier = modifier
    ) {

        BluetoothDeviceList(
            scannedDevices = state.scannedDevices,
            pairedDevices = state.pairedDevices,
            onClick = { device ->
                onDeviceClick(device)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (state.isDiscovering) onStopScan() else onStartScan()
                }
            ) {
                Text(
                    text = if (state.isDiscovering) "Stop scan" else "Start scan"
                )
            }

            Button(
                onClick = {
                    onStartServer()
                }
            ) { Text(text = "Start server") }
        }
    }

}

@Composable
fun BluetoothDeviceList(
    modifier: Modifier = Modifier,
    scannedDevices: List<BluetoothDeviceDomain>,
    pairedDevices: List<BluetoothDeviceDomain>,
    onClick: (BluetoothDeviceDomain) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {

        item {
            Text(
                fontWeight = FontWeight.Bold,
                text = "Paired Devices",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
        }

        items(
            items = pairedDevices
        ) { device ->
            Text(
                text = device.name ?: "(No name)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick(device)
                    }
                    .padding(16.dp)
            )
        }

        item {
            Text(
                fontWeight = FontWeight.Bold,
                text = "Scanned Devices",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
        }

        items(
            items = scannedDevices
        ) { device ->
            Text(
                text = "${device.name ?: "(No name)"}\n${device.address}",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick(device)
                    }
                    .padding(16.dp)
            )
        }
    }
}









