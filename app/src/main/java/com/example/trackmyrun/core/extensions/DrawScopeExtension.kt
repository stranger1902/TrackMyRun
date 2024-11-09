package com.example.trackmyrun.core.extensions

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Size

// useCenter = false to not automatically connect start-point and end-point to the center of the arc
// sweepAngle = 240° to draw an arc from startAngle with sweepAngle size
// startAngle = 150° to start draw arc not from 3pm but from 7pm

fun DrawScope.arcIndicator(
    indicatorStrokeWidth: Float,
    arcContainerSize: Size,
    indicatorColor: Color,
    sweepAngle: Float
) {

    drawArc(
        sweepAngle = sweepAngle,
        size = arcContainerSize,
        color = indicatorColor,
        startAngle = 150f,
        useCenter = false,
        topLeft = Offset(
            y = (size.height - arcContainerSize.height) / 2,
            x = (size.width - arcContainerSize.width) / 2
        ),
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        )
    )
}