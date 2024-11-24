package com.example.trackmyrun.run_friend.presentation.screen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.outlined.Info
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WaitingScreen(
    modifier: Modifier = Modifier,
    text: String
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        Icon(
            contentDescription = "waiting info",
            imageVector = Icons.Outlined.Info,
            modifier = Modifier
                .size(96.dp)
        )

        Text(
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            text = text,
            modifier = Modifier
                .padding(16.dp)
        )

        CircularProgressIndicator()
    }
}