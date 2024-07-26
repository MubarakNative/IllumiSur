package com.mubarak.illumisur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mubarak.illumisur.ui.AndroidSensorEventListener
import com.mubarak.illumisur.ui.MainViewModel
import com.mubarak.illumisur.ui.theme.IllumiSurTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val androidSensorEventListener = AndroidSensorEventListener(this)
        enableEdgeToEdge()
        setContent {
            var lux by remember {
                mutableFloatStateOf(0F)
            }
            IllumiSurTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterListener(
                        lifecycleEventObserver = LocalLifecycleOwner.current,
                        listener = androidSensorEventListener,
                    ) {
                        lux = it
                    }
                    IllumineSurApp(luxValue = lux, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IllumineSurApp(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
    luxValue: Float
) {

    val luxState by viewModel.lux.collectAsStateWithLifecycle()
    viewModel.updateLuxValue(luxValue)

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
            Text(
                text = "Lux: $luxState",
                style = MaterialTheme.typography.displayMedium
            )
    }
}

@Composable
fun RegisterListener(
    lifecycleEventObserver: LifecycleOwner,
    listener: AndroidSensorEventListener,
    lux: (Float) -> Unit = {},
) {

    DisposableEffect(lifecycleEventObserver) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                listener.registerSensor()
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                listener.unregisterSensorListener()
            }
        }
        lifecycleEventObserver.lifecycle.addObserver(observer)

        onDispose {
            lifecycleEventObserver.lifecycle.removeObserver(observer)
            listener.unregisterSensorListener()
        }
    }

    val list = object : AndroidSensorEventListener.LuxValueListener {
        override fun onAzimuthValueChange(luxValue: Float) {
            lux(luxValue)
        }
    }

    listener.setLuxListener(list)
}