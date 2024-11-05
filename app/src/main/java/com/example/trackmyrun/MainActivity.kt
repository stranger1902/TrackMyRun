package com.example.trackmyrun

import com.example.trackmyrun.on_boarding.navigation.registerOnBoardingGraph
import com.example.trackmyrun.on_boarding.navigation.OnBoardingGraph
import com.example.trackmyrun.main.navigation.registerMainGraph
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.trackmyrun.core.theme.TrackMyRunTheme
import androidx.navigation.compose.rememberNavController
import com.example.trackmyrun.main.navigation.MainGraph
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.fillMaxSize
import com.example.trackmyrun.core.utils.UserManager
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.AlertDialog
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.repeatOnLifecycle
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.content.pm.PackageManager
import androidx.compose.material3.Text
import androidx.lifecycle.Lifecycle
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import android.provider.Settings
import android.content.Intent
import javax.inject.Inject
import android.os.Bundle
import android.os.Build
import android.Manifest
import android.net.Uri

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @Inject lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val permissions = mutableListOf(
            // Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                add(Manifest.permission.POST_NOTIFICATIONS)
        }.toList()

        setContent {

            var permissionGranted by rememberSaveable {
                mutableStateOf(true)
            }

            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        permissions.forEach { permission ->
                            permissionGranted = permissionGranted && checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
                        }
                    }
                }
            }

            TrackMyRunTheme {

                val navController = rememberNavController()

                if (!permissionGranted)
                    AlertDialog(
                        text = {
                            Text(text = "L'applicazione ha bisogno di tutti i permessi necessari per poter funzionare correttamente")
                        },
                        title = {
                            Text(text = "Permessi necessari")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", packageName, null)
                                    ).also { startActivity(it) }
                                }
                            ) { Text(text = "Apri impostazioni") }
                        },
                        onDismissRequest = { }
                    )

                NavHost(
                    startDestination = if (!userManager.checkUser()) OnBoardingGraph else MainGraph,
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    registerOnBoardingGraph(
                        onBoardingCompleted = {
                            navController.navigate(MainGraph) {
                                popUpTo<OnBoardingGraph> {
                                    inclusive = true
                                }
                            }
                        }
                    )

                    registerMainGraph(
                        navController = navController
                    )
                }
            }
        }
    }

}