package com.example.trackmyrun.run_new.presentation.component

import com.example.trackmyrun.run_new.presentation.CurrentRunState
import com.example.trackmyrun.core.extensions.toStopwatchFormat
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import com.example.trackmyrun.core.extensions.toKmH
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackmyrun.R
import kotlin.math.roundToInt

@Composable
fun NewRunController(
    modifier: Modifier = Modifier,
    currentRun: CurrentRunState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit
) {

    Card (
        colors = CardDefaults.cardColors().copy(
            containerColor = CardDefaults.cardColors().containerColor.copy(alpha = 0.75f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Icon(
                    painter = painterResource(R.drawable.ic_stopwatch),
                    contentDescription = "stopwatch timer",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(32.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = currentRun.timeElapsedMillis.toStopwatchFormat(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 42.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                ) {

                    Icon(
                        painter = painterResource(R.drawable.ic_calories),
                        contentDescription = "calories",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "${currentRun.kcalBurned.roundToInt()} Kcal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                ) {

                    Icon(
                        painter = painterResource(R.drawable.ic_speed),
                        contentDescription = "avarage speed",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "${"%.2f".format(currentRun.avgSpeedMs.toKmH())} Km/h",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Icon(
                    painter = painterResource(R.drawable.ic_tracking),
                    contentDescription = "distance",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "${"%.2f".format(currentRun.distanceMeters)} m",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                OutlinedButton (
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        if (currentRun.isTracking) onPauseClick() else onStartClick()
                    }
                ) { Text(text = if (currentRun.isTracking) "PAUSE" else "START") }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        onStopClick()
                    }
                ) { Text(text = "STOP") }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NewRunControllerPreview() {
    MaterialTheme {
        NewRunController(
            currentRun = CurrentRunState(
                timeElapsedMillis = 35782362,
                distanceMeters = 3256f,
                isTracking = false,
                kcalBurned = 356f
            ),
            onStartClick = { },
            onPauseClick = { },
            onStopClick = { }
        )
    }
}
