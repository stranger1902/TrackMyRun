package com.example.trackmyrun

import com.example.trackmyrun.bluetooth.presentation.component.ObserveAsEvents
import com.example.trackmyrun.on_boarding.navigation.registerOnBoardingGraph
import com.example.trackmyrun.bluetooth.domain.chat.BluetoothController
import com.example.trackmyrun.on_boarding.navigation.OnBoardingGraph
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.trackmyrun.main.navigation.registerMainGraph
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trackmyrun.core.utils.PermissionManager
import androidx.datastore.preferences.preferencesDataStore
import com.example.trackmyrun.core.theme.TrackMyRunTheme
import androidx.navigation.compose.rememberNavController
import com.example.trackmyrun.main.navigation.MainGraph
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.preferences.core.Preferences
import androidx.compose.foundation.layout.fillMaxSize
import com.example.trackmyrun.core.utils.UserManager
import com.example.trackmyrun.core.utils.Constants
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.AlertDialog
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.repeatOnLifecycle
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import android.bluetooth.BluetoothAdapter
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.datastore.core.DataStore
import androidx.compose.material3.Text
import androidx.lifecycle.Lifecycle
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import android.provider.Settings
import android.content.Context
import android.content.Intent
import javax.inject.Inject
import android.os.Bundle
import android.net.Uri

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.USER_DATASTORE_KEY)

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @Inject lateinit var bluetoothController: BluetoothController
    @Inject lateinit var permissionManager: PermissionManager
    @Inject lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        bluetoothController.registerBluetoothReceivers()

//        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//
//        if (!bluetoothManager.adapter.isEnabled) {
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//                /* We don't need to elaborate the result... */
//            }.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
//        }

        setContent {

            val permissionGranted by permissionManager.permissionGranted.collectAsStateWithLifecycle()

            val coroutineScope = rememberCoroutineScope()

            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                /* We don't need to elaborate the result... */
            }

            ObserveAsEvents(bluetoothController.makeDiscoverable) { makeDiscoverable ->
                if (makeDiscoverable)
                    Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, Constants.BLUETOOTH_DISCOVERABLE_INTERVAL_SEC)
                    }.also {
                        launcher.launch(it)
                    }
            }

            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        permissionManager.checkPermissions()
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
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
                                        .also { startActivity(it) }
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

    override fun onDestroy() {
        super.onDestroy().also {
            bluetoothController.release()
        }
    }

}