package com.example.trackmyrun.run_detail.presentation

import com.example.trackmyrun.run_detail.presentation.component.RunDetail
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.trackmyrun.core.domain.model.RunModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunDetailScaffoldScreen(
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        fontWeight = FontWeight.Bold,
                        text = "Dettaglio corsa",
                        fontSize = 32.sp
                    )
                }
            )
        },
    ) { innerPadding ->

        RunDetail(
            onBackPressed = onBackPressed,
            profileImage = imageBitmap,
            runDetail = runDetail,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        )
    }
}