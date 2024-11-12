package com.example.trackmyrun.bluetooth.presentation.component

import com.example.trackmyrun.bluetooth.presentation.BluetoothUiState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    state: BluetoothUiState,
    onSendMessage: (message: String) -> Unit,
    onDisconnect: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    var message by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                text = "Messaggi",
                modifier = Modifier
                    .weight(1f)
            )

            IconButton(
                onClick = {
                    onDisconnect()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "disconnect"
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(
                items = state.messages
            ) { message ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ChatMessage(
                        message = message,
                        modifier = Modifier
                            .align(if (message.isFromLocalUser) Alignment.End else Alignment.Start)
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = message,
                placeholder = {
                    Text(text = "Messaggio")
                },
                onValueChange = {
                    message = it
                },
                modifier = Modifier
                    .weight(1f)
            )

            IconButton(
                onClick = {
                    onSendMessage(message)
                    keyboardController?.hide()
                    message = ""
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "send message"
                )
            }
        }
    }
}