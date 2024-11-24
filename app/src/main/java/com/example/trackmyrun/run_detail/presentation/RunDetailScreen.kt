package com.example.trackmyrun.run_detail.presentation

import com.example.trackmyrun.run_detail.presentation.component.RunDetail
import com.example.trackmyrun.core.domain.model.RunModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RunDetailScreen(
    modifier: Modifier = Modifier,
    runId: String,
    onBackPressed: () -> Unit
) {

    val viewModel = hiltViewModel<RunDetailViewModel>()

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
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            fontWeight = FontWeight.Bold,
            text = "Dettaglio corsa",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        RunDetail(
            onBackPressed = onBackPressed,
            profileImage = imageBitmap,
            runDetail = runDetail,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}