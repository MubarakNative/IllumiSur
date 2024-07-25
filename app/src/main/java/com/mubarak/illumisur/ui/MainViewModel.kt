package com.mubarak.illumisur.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {
    private val _azimuth = MutableStateFlow(0f)
    val lux: StateFlow<Float> = _azimuth.asStateFlow()

    fun updateLuxValue(azimuth:Float){
        _azimuth.value = azimuth
    }

}