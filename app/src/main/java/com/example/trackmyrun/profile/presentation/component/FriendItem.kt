package com.example.trackmyrun.profile.presentation.component

import com.example.trackmyrun.core.domain.model.FriendModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackmyrun.R

@Composable
fun FriendItem(
    modifier: Modifier = Modifier,
    item: FriendModel
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_handshake),
            contentDescription = "handshake",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = item.name,
            fontSize = 20.sp
        )
    }
}