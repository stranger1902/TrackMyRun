package com.example.trackmyrun.run_friend.presentation.component

import com.example.trackmyrun.bluetooth.domain.chat.BluetoothDeviceDomain
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                text = "Dispositivi associati",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(12.dp)
            )
        }

        items(
            items = pairedDevices
        ) { device ->
            Text(
                text = "${device.name ?: "(No name)"}\n${device.address}",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick(device)
                    }
                    .padding(8.dp)
            )
        }

        item {
            Text(
                fontWeight = FontWeight.Bold,
                text = "Altri dispositivi",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(12.dp)
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
                    .padding(8.dp)
            )
        }
    }
}
