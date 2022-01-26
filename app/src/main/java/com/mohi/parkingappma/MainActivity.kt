package com.mohi.parkingappma

import android.app.Activity
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.mohi.parkingappma.model.viewmodel.EntitiesViewModel
import com.mohi.parkingappma.navigation.AppNavigation
import com.mohi.parkingappma.ui.theme.ParkingAppMATheme
import com.mohi.parkingappma.utils.InternetCallback
import com.mohi.parkingappma.utils.InternetStatus
import com.mohi.parkingappma.utils.InternetStatusReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private var lastState = InternetStatus.OFFLINE

    val viewModel : EntitiesViewModel by viewModels()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        setContent {
            ParkingAppMATheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    App()
                }
            }
        }

        initReceiver()
    }

    private fun initReceiver() {
        InternetStatusReceiver.setOnCallbackReceivedListener(object : InternetCallback {
            override fun onStatusChanged(status: InternetStatus) {
                if(status!=lastState) {
                    when (status) {
                        InternetStatus.ONLINE -> {
                            Log.d("Mohi", "Back online!")
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.backOnline()
                            }
                        }
                        InternetStatus.OFFLINE -> {
                            Log.d("Mohi", "Went offline!")
                        }
                    }
                    lastState=status
                }
            }
        })
        registerReceiver(
            InternetStatusReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(InternetStatusReceiver)
    }

    companion object {
        lateinit var instance : Activity
    }
}

@ExperimentalMaterialApi
@Composable
fun App(){
    AppNavigation()
}