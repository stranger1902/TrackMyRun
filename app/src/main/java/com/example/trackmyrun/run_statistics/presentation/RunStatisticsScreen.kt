package com.example.trackmyrun.run_statistics.presentation

import com.example.trackmyrun.core.extensions.toStopwatchUserFormat
import com.example.trackmyrun.core.domain.model.RunStatisticsModel
import com.example.trackmyrun.core.extensions.shimmerEffect
import com.example.trackmyrun.core.extensions.conditional
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import com.example.trackmyrun.core.extensions.msToKmH
import androidx.compose.foundation.layout.Arrangement
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trackmyrun.core.extensions.mToKm
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackmyrun.R

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<RunStatisticsViewModel>()

    var runStatistics: RunStatisticsModel? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit) {
        runStatistics = viewModel.getStatistics().await()
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = "Corse totali",
                fontSize = 16.sp
            )

            Text(
                text = runStatistics?.let { "${it.totalRun}" } ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                modifier = Modifier
                    .conditional(runStatistics == null) {
                        shimmerEffect(1500)
                    }
                    .padding(8.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {

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

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Distanza totale percorsa",
                    fontSize = 16.sp
                )
            }

            Text(
                text = runStatistics?.let { "${"%.3f".format(it.totalDistanceMeters.mToKm())} Km" } ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                modifier = Modifier
                    .conditional(runStatistics == null) {
                        shimmerEffect(1500)
                    }
                    .padding(8.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
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
                        .size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Durata totale",
                    fontSize = 16.sp
                )
            }

            Text(
                text = runStatistics?.totalDurationMillis?.toStopwatchUserFormat() ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                modifier = Modifier
                    .conditional(runStatistics == null) {
                        shimmerEffect(1500)
                    }
                    .padding(8.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Icon(
                    painter = painterResource(R.drawable.ic_speed),
                    contentDescription = "avarage speed",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Velocità media totale",
                    fontSize = 16.sp
                )
            }

            Text(
                text = runStatistics?.let { "${"%.2f".format(it.totalAvgSpeedMs.msToKmH())} Km/h" } ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                modifier = Modifier
                    .conditional(runStatistics == null) {
                        shimmerEffect(1500)
                    }
                    .padding(8.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Icon(
                    painter = painterResource(R.drawable.ic_calories),
                    contentDescription = "calories",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "totale calorie bruciate",
                    fontSize = 16.sp
                )
            }

            Text(
                text = runStatistics?.let { "${"%.2f".format(it.totalKcalBurned)} Kcal" } ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                modifier = Modifier
                    .conditional(runStatistics == null) {
                        shimmerEffect(1500)
                    }
                    .padding(8.dp)
            )
        }
    }
}