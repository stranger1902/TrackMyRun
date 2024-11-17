package com.example.trackmyrun.run_new.presentation

import com.example.trackmyrun.run_new.presentation.component.CountdownIndicator
import com.example.trackmyrun.run_new.presentation.component.NewRunController
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.MapsComposeExperimentalApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.navigationBars
import com.example.trackmyrun.core.domain.model.RunModel
import com.example.trackmyrun.core.domain.model.toLatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.CameraPosition
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import com.google.android.gms.maps.CameraUpdateFactory
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.fillMaxSize
import com.google.android.gms.maps.model.LatLngBounds
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.example.trackmyrun.core.utils.Constants
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.layout.padding
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import androidx.compose.ui.platform.LocalContext
import com.google.maps.android.ktx.awaitSnapshot
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.MapType
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import kotlin.uuid.ExperimentalUuidApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import android.provider.Settings
import com.example.trackmyrun.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.async
import android.content.Intent
import android.widget.Toast
import kotlin.uuid.Uuid

@OptIn(MapsComposeExperimentalApi::class, ExperimentalUuidApi::class)
@Composable
fun NewRunScreen(
    modifier: Modifier = Modifier,
    onNavigateToRunDetailScreen: (run: RunModel) -> Unit
) {
    Box(
        modifier = modifier
    ) {

        val viewModel = hiltViewModel<NewRunViewModel>()

        val countdownIsRunning by viewModel.countdownIsRunning.collectAsStateWithLifecycle()
        val isGpsEnabled by viewModel.isGpsEnabled.collectAsStateWithLifecycle()
        val countdown by viewModel.countdown.collectAsStateWithLifecycle()
        val currentRun by viewModel.state.collectAsStateWithLifecycle()

        val cameraPositionState = rememberCameraPositionState()

        val coroutineScope = rememberCoroutineScope()

        val isDarkMode = isSystemInDarkTheme()

        val context = LocalContext.current

        var isMyLocationEnabled by rememberSaveable {
            mutableStateOf(true)
        }

        var takeSnapshot by rememberSaveable {
            mutableStateOf(false)
        }

        var isMapLoaded by rememberSaveable {
            mutableStateOf(false)
        }

        val uiSettings by remember {
            mutableStateOf(
                MapUiSettings(
                    scrollGesturesEnabledDuringRotateOrZoom = false,
                    myLocationButtonEnabled = false,
                    zoomControlsEnabled = false
                )
            )
        }

        val mapsStyle: MapStyleOptions? by remember {
            mutableStateOf(
                if (isDarkMode) MapStyleOptions.loadRawResourceStyle(context, R.raw.maps_night_mode) else null
            )
        }

        val properties by remember(isMyLocationEnabled) {
            mutableStateOf(
                MapProperties(
                    isMyLocationEnabled = isMyLocationEnabled,
                    mapStyleOptions = mapsStyle,
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

        if (!isGpsEnabled)
            AlertDialog(
                text = {
                    Text(text = "Hai bisogno del GPS abilitato per poter tracciare il tuo percorso")
                },
                title = {
                    Text(text = "GPS disabilitato")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        }
                    ) { Text(text = "Apri impostazioni") }
                },
                onDismissRequest = { }
            )

        // disable back-press button while new run is saving..
        BackHandler(
            enabled = takeSnapshot
        ) {  }

        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            modifier = Modifier
                .fillMaxSize(),
            onMapLoaded = {
                isMapLoaded = true
            }
        ) {

            MapEffect(takeSnapshot) { map ->

                if (takeSnapshot) {

                    if (currentRun.pathPointList.firstOrNull()?.isEmpty() == true) {
                        viewModel.stopRunning()
                        return@MapEffect
                    }

                    isMyLocationEnabled = false

                    LatLngBounds.Builder().apply {
                        currentRun.pathPointList.flatMap { pathPoint ->
                            pathPoint.map {
                                include(it.toLatLng())
                            }
                        }
                    }.build().also {
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngBounds(it, Constants.MAPS_SNAPSHOT_PADDING)
                        )
                    }

                    val run = RunModel(
                        startTimestamp = System.currentTimeMillis() - currentRun.timeElapsedMillis,
                        durationMillis = currentRun.timeElapsedMillis,
                        distanceMeters = currentRun.distanceMeters,
                        kcalBurned = currentRun.kcalBurned,
                        avgSpeedMs = currentRun.avgSpeedMs,
                        id = Uuid.random().toHexString()
                    )

                    coroutineScope.launch {

                        map.awaitSnapshot()?.let {

                            val deferredResult = async {
                                viewModel.saveSnapshot(it, run.id)
                                delay(3000)
                            }

                            viewModel.saveRun(run)

                            // i need to wait snapshot saving is completed before to navigate to RunDetailScreen, otherwise, it will be partially showed..
                            deferredResult.await()

                            viewModel.stopRunning()

                            onNavigateToRunDetailScreen(run)
                            // takeSnapshot = false
                        }
                    }
                }
            }

            currentRun.pathPointList.map { pathPoint ->
                Polyline(
                    points = pathPoint.map { it.toLatLng() },
                    width = Constants.POLYLINE_WIDTH
                )
            }
        }

        if (countdownIsRunning)
            Dialog(
                properties = DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = true
                ),
                onDismissRequest = {
                    viewModel.skipCountdown()
                }
            ) {
                CountdownIndicator(
                    maxIndicatorValue = Constants.RUN_COUNTDOWN_INITIAL_VALUE,
                    countdown = countdown,
                    size = 240.dp,
                    onSkipTimerClick = {
                        viewModel.skipCountdown()
                    }
                )
            }

        if (!takeSnapshot)
            NewRunController(
                currentRun = currentRun,
                onStartClick = {
                    if (!isMapLoaded)
                        Toast.makeText(context, "Attendere che la mappa sia completamente caricata", Toast.LENGTH_SHORT).show()
                    else
                        viewModel.startRunning()
                },
                onPauseClick = {
                    viewModel.pauseRunning()
                },
                onStopClick = {
                    if (currentRun.pathPointList.isEmpty()) {
                        Toast.makeText(context, "Iniziare la corsa", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.pauseRunning()
                        takeSnapshot = true
                    }
                },
                modifier = Modifier
                    .padding(WindowInsets.navigationBars.asPaddingValues())
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
            )
    }
}