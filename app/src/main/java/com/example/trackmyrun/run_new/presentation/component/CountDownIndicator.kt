package com.example.trackmyrun.run_new.presentation.component

import com.example.trackmyrun.core.extensions.arcIndicator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Canvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.ui.text.drawText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CountdownIndicator(
    modifier: Modifier = Modifier,
    backgroundIndicatorColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
    foregroundIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    countdownTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
    countdownTextColor: Color = MaterialTheme.colorScheme.onSurface,
    backgroundIndicatorStrokeWidth: Float = 100f,
    foregroundIndicatorStrokeWidth: Float = 100f,
    textSuffix: String = "s",
    maxIndicatorValue: Int,
    countdown: Int,
    size: Dp,
    onSkipTimerClick: () -> Unit
) {

    var countdownState by rememberSaveable {
        mutableIntStateOf(countdown)
    }

    LaunchedEffect(countdown) {
        countdownState = countdown.coerceAtMost(maxIndicatorValue)
    }

    val animatedSweepAngle by animateFloatAsState(
        targetValue = (240 * countdownState / maxIndicatorValue.toFloat()),
        animationSpec = tween(0),
        label = "sweep angle"
    )

    val textMeasurer = rememberTextMeasurer()

    val countdownText = "$countdownState $textSuffix"

    val bigTextLayoutResult = remember(countdownText) {
        textMeasurer.measure(countdownText, countdownTextStyle)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {

        Canvas(
            modifier = Modifier
                .size(size.coerceAtLeast(240.dp))
        ) {

            val arcContainerSize = this.size * 0.8f

            arcIndicator(
                indicatorStrokeWidth = backgroundIndicatorStrokeWidth,
                indicatorColor = backgroundIndicatorColor,
                arcContainerSize = arcContainerSize,
                sweepAngle = 240f
            )

            arcIndicator(
                indicatorStrokeWidth = foregroundIndicatorStrokeWidth,
                indicatorColor = foregroundIndicatorColor,
                arcContainerSize = arcContainerSize,
                sweepAngle = animatedSweepAngle
            )

            drawText(
                textMeasurer = textMeasurer,
                text = countdownText,
                topLeft = Offset(
                    y = (this.size.height - bigTextLayoutResult.size.height) / 2,
                    x = (this.size.width - bigTextLayoutResult.size.width) / 2,
                ),
                style = countdownTextStyle.copy(
                    color = countdownTextColor
                )
            )
        }

        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = {
                onSkipTimerClick()
            }
        ) { Text(text = "Salta timer") }
    }
}

@Composable
@Preview(showBackground = true)
fun CountDownIndicatorPreview() {
    MaterialTheme {
        CountdownIndicator(
            maxIndicatorValue = 100,
            countdown = 40,
            size = 240.dp,
            onSkipTimerClick = { }
        )
    }
}