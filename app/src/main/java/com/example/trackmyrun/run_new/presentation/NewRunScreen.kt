package com.example.trackmyrun.run_new.presentation

import com.google.maps.android.compose.rememberCameraPositionState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trackmyrun.core.domain.model.toLatLng
import com.google.android.gms.maps.model.CameraPosition
import androidx.compose.foundation.layout.fillMaxWidth
import com.google.android.gms.maps.CameraUpdateFactory
import androidx.compose.foundation.layout.fillMaxSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.example.trackmyrun.core.utils.Constants
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewRunScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {

        val viewModel = hiltViewModel<NewRunViewModel>()

        val currentRun by viewModel.state.collectAsStateWithLifecycle()

        val cameraPositionState = rememberCameraPositionState()

        val uiSettings by remember {
            mutableStateOf(
                MapUiSettings(
                    scrollGesturesEnabledDuringRotateOrZoom = false,
                    myLocationButtonEnabled = false,
                    zoomControlsEnabled = false
                )
            )
        }

        val properties by remember {
            mutableStateOf(
                MapProperties(
                    isMyLocationEnabled = true,
                    mapType = MapType.NORMAL
                )
            )
        }

        LaunchedEffect(key1 = currentRun.lastPathPoint) {
            currentRun.lastPathPoint?.let {
                cameraPositionState.animate(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(it.toLatLng(), Constants.MAPS_ZOOM)
                    )
                )
            }
        }

        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            modifier = Modifier
                .fillMaxSize()
        ) {

        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
                .fillMaxWidth()
        ) {

            Button(
                onClick = {
                    viewModel.startRunning()
                }
            ) { Text(text = "START") }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.pauseRunning()
                }
            ) { Text(text = "PAUSE") }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.stopRunning()
                }
            ) { Text(text = "STOP") }
        }
    }
}