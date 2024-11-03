package com.example.trackmyrun.on_boarding.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PagerIndicator(
    currentPage: Int,
    size: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        repeat(size) {
            PagerIndicatorItem(
                isSelected = it == currentPage
            )
        }
    }
}

@Composable
fun PagerIndicatorItem(
    isSelected: Boolean
) {

    val height = 12.dp

    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else height,
        label = "pager indicator item width"
    )

    Box(
        modifier = Modifier
            .padding(3.dp)
            .width(width.value)
            .height(height)
            .clip(CircleShape)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Gray.copy(alpha = 0.5f)
            )
    )
}

@Composable
@Preview(showBackground = true)
fun PagerIndicatorPreview() {
    MaterialTheme {
        PagerIndicator(
            currentPage = 1,
            size = 4
        )
    }
}