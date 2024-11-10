package com.example.trackmyrun

import com.example.trackmyrun.on_boarding.navigation.registerOnBoardingGraph
import com.example.trackmyrun.on_boarding.navigation.OnBoardingGraph
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

import com.example.trackmyrun.tmp_bluetooth.presentation.BluetoothScreen
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.os.Build
import android.Manifest

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.USER_DATASTORE_KEY)

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @Inject lateinit var permissionManager: PermissionManager
    @Inject lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()





        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val bluetoothAdapter = bluetoothManager.adapter

        val isBluetoothEnabled = bluetoothAdapter.isEnabled

        val enableBluetoothLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            /* Not needed */
        }

        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            val canEnableBluetooth = perms[if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Manifest.permission.BLUETOOTH_CONNECT
            } else {
                true
            }] == true

            if (canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                )
            )





        setContent {

            val permissionGranted by permissionManager.permissionGranted.collectAsStateWithLifecycle()

            val coroutineScope = rememberCoroutineScope()

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
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", packageName, null)
                                    ).also { startActivity(it) }
                                }
                            ) { Text(text = "Apri impostazioni") }
                        },
                        onDismissRequest = { }
                    )

                Scaffold { innerPadding ->
                    BluetoothScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }

//                NavHost(
//                    startDestination = if (!userManager.checkUser()) OnBoardingGraph else MainGraph,
//                    navController = navController,
//                    modifier = Modifier
//                        .fillMaxSize()
//                ) {
//
//                    registerOnBoardingGraph(
//                        onBoardingCompleted = {
//                            navController.navigate(MainGraph) {
//                                popUpTo<OnBoardingGraph> {
//                                    inclusive = true
//                                }
//                            }
//                        }
//                    )
//
//                    registerMainGraph(
//                        navController = navController
//                    )
//                }
            }
        }
    }
}