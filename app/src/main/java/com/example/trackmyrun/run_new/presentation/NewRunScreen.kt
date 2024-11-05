package com.example.trackmyrun.run_new.presentation

import com.example.trackmyrun.run_new.presentation.component.NewRunController
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
import androidx.compose.foundation.layout.offset
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.MapType
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

            currentRun.pathPointList.map { pathPoint ->
                Polyline(
                    points = pathPoint.map { it.toLatLng() },
                    width = Constants.POLYLINE_WIDTH
                )
            }

        }

        NewRunController(
            currentRun = currentRun,
            onStartClick = {
                viewModel.startRunning()
            },
            onPauseClick = {
                viewModel.pauseRunning()
            },
            onStopClick = {
                viewModel.stopRunning()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-32).dp)
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}