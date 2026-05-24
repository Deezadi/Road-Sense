package com.example.potholedetector

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

data class PotholeEvent(val force: Float, val time: String)

object AppState : SensorEventListener {

    var isDetecting  = false
    var currentAccel = 0f
    var potholeCount = 0
    var lastForce    = 0f
    var peakForce    = 0f
    var avgForce     = 0f
    var threshold    = 15f
    var statusState  = "idle"   // "idle" | "detecting" | "pothole"
    val history      = mutableListOf<PotholeEvent>()

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var lastPotholeTime = 0L
    private val readings = mutableListOf<Float>()

    fun init(sm: SensorManager) {
        sensorManager = sm
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun startDetection() {
        isDetecting = true
        statusState = "detecting"
        sensorManager?.registerListener(this, accelerometer,
            SensorManager.SENSOR_DELAY_GAME)
    }

    fun stopDetection() {
        isDetecting = false
        statusState = "idle"
        sensorManager?.unregisterListener(this)
    }

    fun reset() {
        stopDetection()
        potholeCount = 0
        currentAccel = 0f
        lastForce    = 0f
        peakForce    = 0f
        avgForce     = 0f
        statusState  = "idle"
        history.clear()
        readings.clear()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isDetecting || event == null) return
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val force = sqrt((x*x + y*y + z*z).toDouble()).toFloat()
        currentAccel = force
        readings.add(force)
        if (readings.size > 100) readings.removeAt(0)
        avgForce = readings.average().toFloat()
        if (force > peakForce) peakForce = force

        val now = System.currentTimeMillis()
        if (force > threshold && now - lastPotholeTime > 1200) {
            lastPotholeTime = now
            potholeCount++
            lastForce   = force
            statusState = "pothole"
            val time = java.text.SimpleDateFormat("HH:mm:ss",
                java.util.Locale.getDefault()).format(java.util.Date())
            history.add(0, PotholeEvent(force, time))
            android.os.Handler(android.os.Looper.getMainLooper())
                .postDelayed({ if (isDetecting) statusState = "detecting" }, 1800)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}