package com.example.trackmyrun.run_detail.presentation

import com.example.trackmyrun.core.extensions.toStopwatchUserFormat
import com.example.trackmyrun.core.extensions.toDateTimeFormat
import com.example.trackmyrun.core.extensions.shimmerEffect
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.trackmyrun.core.extensions.conditional
import com.example.trackmyrun.core.domain.model.RunModel
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import com.example.trackmyrun.core.extensions.msToKmH
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trackmyrun.core.extensions.mToKm
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackmyrun.R

@Composable
fun RunDetailScreen(
    modifier: Modifier = Modifier,
    runId: String
) {

    val viewModel = hiltViewModel<RunDetailViewModel>()

    val scrollState = rememberScrollState()

    var imageBitmap: ImageBitmap? by remember {
        mutableStateOf(null)
    }

    var runDetail: RunModel? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit) {
        imageBitmap = viewModel.loadImage(runId).await()?.asImageBitmap()
        runDetail = viewModel.getRunDetail(runId).await()
    }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {

        if (imageBitmap != null)
            Image(
                contentDescription = "snapshot run map",
                contentScale = ContentScale.FillBounds,
                bitmap = imageBitmap!!,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(imageBitmap!!.width / imageBitmap!!.height.toFloat())
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        else
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2 / 3f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = runDetail?.startTimestamp?.toDateTimeFormat() ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .conditional(runDetail == null) {
                            shimmerEffect(1500)
                        }
                )
            }

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
                    text = runDetail?.let { "durata corsa: ${it.durationMillis.toStopwatchUserFormat()}" } ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .conditional(runDetail == null) {
                            shimmerEffect(1500)
                        }
                )
            }

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
                    text = runDetail?.let { "distanza percorsa: ${"%.3f".format(it.distanceMeters.mToKm())} Km" } ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .conditional(runDetail == null) {
                            shimmerEffect(1500)
                        }
                )
            }

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
                    text = runDetail?.let { "calorie bruciate: ${"%.2f".format(it.kcalBurned)} Kcal" } ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .conditional(runDetail == null) {
                            shimmerEffect(1500)
                        }
                )
            }

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
                    text = runDetail?.let { "velocità media: ${"%.2f".format(it.avgSpeedMs.msToKmH())} Km/h" } ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .conditional(runDetail == null) {
                            shimmerEffect(1500)
                        }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}