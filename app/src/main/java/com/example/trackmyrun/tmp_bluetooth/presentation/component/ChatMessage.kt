package com.example.trackmyrun.tmp_bluetooth.presentation.component

import com.example.trackmyrun.tmp_bluetooth.domain.chat.BluetoothMessage
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatMessage(
    modifier: Modifier = Modifier,
    message: BluetoothMessage
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {

        Text(
            text = message.senderName,
            fontSize = 10.sp
        )

        Text(
            text = message.message,
            modifier = Modifier
                .widthIn(max = 250.dp)
        )
    }
}