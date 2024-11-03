package com.example.trackmyrun.on_boarding.presentation.screen

import com.example.trackmyrun.on_boarding.data.local.OnBoardingModel
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThirdScreen(
    modifier: Modifier = Modifier,
    item: OnBoardingModel
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        Image(
            contentDescription = "third screen image",
            painter = painterResource(item.resImage),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(5/4f)
        )

        Spacer(Modifier.height(40.dp))

        Text(
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp,
            text = item.title,
            fontSize = 36.sp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Text(
            textAlign = TextAlign.Start,
            text = item.description,
            lineHeight = 24.sp,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}