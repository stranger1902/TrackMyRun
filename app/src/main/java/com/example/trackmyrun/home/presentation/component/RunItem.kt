package com.example.trackmyrun.home.presentation.component

import com.example.trackmyrun.core.extensions.toDateTimeFormat
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.trackmyrun.home.domain.model.RunModel
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.rememberCoroutineScope
import com.example.trackmyrun.core.extensions.msToKmH
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Arrangement
import com.example.trackmyrun.core.extensions.mToKm
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import kotlinx.coroutines.Dispatchers
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Deferred
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import com.example.trackmyrun.R
import android.graphics.Bitmap

@Composable
fun RunItem(
    modifier: Modifier = Modifier,
    snapshot: suspend () -> Deferred<Bitmap?>,
    item: RunModel
) {

    val coroutineScope = rememberCoroutineScope()

    var imageBitmap: ImageBitmap? by remember {
        mutableStateOf(null)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        coroutineScope.launch {
            imageBitmap = snapshot().await()?.asImageBitmap()
        }

        if (imageBitmap != null)
            Image(
                contentDescription = "snapshot run map",
                contentScale = ContentScale.FillBounds,
                bitmap = imageBitmap!!,
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(2/3f)
                    .clip(RoundedCornerShape(8.dp))
            )
        else
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(2/3f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Icon(
                    painter = painterResource(R.drawable.ic_stopwatch),
                    contentDescription = "stopwatch timer",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.startTimestamp.toDateTimeFormat(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Icon(
                    painter = painterResource(R.drawable.ic_tracking),
                    contentDescription = "distance",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "distanza percorsa: ${"%.3f".format(item.distanceMeters.mToKm())} Km",
                    fontSize = 12.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Icon(
                    painter = painterResource(R.drawable.ic_calories),
                    contentDescription = "calories",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "calorie bruciate: ${"%.2f".format(item.kcalBurned)} Kcal",
                    fontSize = 12.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Icon(
                    painter = painterResource(R.drawable.ic_speed),
                    contentDescription = "avarage speed",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "velocità media: ${"%.2f".format(item.avgSpeedMs.msToKmH())} Km/h",
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RunItemPreview() {
    MaterialTheme {
        RunItem(
            snapshot = {
                CoroutineScope(Dispatchers.Default).async {
                    null
                }
            },
            item = RunModel(
                startTimestamp = 1730910997000,
                durationMillis = 2374323,
                distanceMeters = 2378f,
                id = "35823975298341",
                kcalBurned = 3247f,
                avgSpeedMs = 3f
            )
        )
    }
}