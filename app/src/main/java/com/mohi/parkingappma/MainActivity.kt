package com.mohi.parkingappma

import android.app.Activity
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.google.gson.Gson
import com.mohi.parkingappma.model.domain.Entity
import com.mohi.parkingappma.model.viewmodel.EntitiesViewModel
import com.mohi.parkingappma.navigation.AppNavigation
import com.mohi.parkingappma.ui.theme.ParkingAppMATheme
import com.mohi.parkingappma.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class MainActivity: ComponentActivity(), MessageListener {
    private var lastState = InternetStatus.OFFLINE

    val viewModel : EntitiesViewModel by viewModels()
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectToSocket()

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

    private fun connectToSocket() {
        WebSocketManager.init("http://192.168.1.6:2021/", this)
        WebSocketManager.connect(WebSocketManager.createListener())
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

    override fun onConnectSuccess() {
        Log.d("Mohi","Socket conn success")
    }

    override fun onConnectFailed() {
        Log.d("Mohi","Socket conn failed")
    }

    override fun onClose() {
        Log.d("Mohi","Socket close")
    }

    override fun onMessage(text: String?) {
        Log.d("Mohi","Recv brdcast $text")
        val entity = Gson().fromJson<Entity>(text, Entity::class.java)
        runOnUiThread {
            Toast.makeText(this, "Server created new $entity", Toast.LENGTH_SHORT).show()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun App(){
    AppNavigation()
}
