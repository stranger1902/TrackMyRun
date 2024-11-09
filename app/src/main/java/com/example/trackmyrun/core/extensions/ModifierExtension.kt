package com.example.trackmyrun.core.extensions

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.animation.core.animateFloat
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.background
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

fun Modifier.shimmerEffect(millis: Int): Modifier = composed {

    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(
        label = "shimmer"
    )

    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        label = "x-offset",
        animationSpec = infiniteRepeatable(
            animation = tween(millis)
        )
    )

    background(
        brush = Brush.linearGradient(
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat()),
            start = Offset(startOffsetX, 0f),
            colors = listOf(
                Color(0xFFB8B8B8),
                Color(0xFF949191),
                Color(0xFFB8B8B8)
            )
        )
    ).onGloballyPositioned {
        size = it.size
    }
}