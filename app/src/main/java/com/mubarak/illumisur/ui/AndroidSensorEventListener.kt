package com.mubarak.illumisur.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class AndroidSensorEventListener(
    context: Context
) : SensorEventListener {

    private val lightSensorLux = FloatArray(1)

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    interface LuxValueListener {
        fun onAzimuthValueChange(luxValue: Float) //cohesive approach
    }

    private var luxValueListener: LuxValueListener?= null

    override fun onSensorChanged(event: SensorEvent) {

        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            System.arraycopy(event.values, 0, lightSensorLux, 0, lightSensorLux.size)
        }
            luxValueListener?.onAzimuthValueChange(lightSensorLux[0])
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun registerSensor() {
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    fun unregisterSensorListener() {
        sensorManager.unregisterListener(this)
    }

    fun setLuxListener(listener: LuxValueListener) {
        luxValueListener = listener
    }
}