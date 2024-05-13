package rma.lv1.viewmodel

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.sqrt

class StepsViewModel: ViewModel(), SensorEventListener {
    private val db = Firebase.firestore

    private val _steps = MutableLiveData<Int>()
    val steps: LiveData<Int> = _steps

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor


    fun setSensor(sensorManager: SensorManager) {
        this.sensorManager = sensorManager
    }

    fun registerSensors() {

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            this.sensor = it
        }

        sensorManager.registerListener(this, this.sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    fun unregisterSensors() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val alpha: Float = 0.8f
            var gravity = floatArrayOf(0f, 0f, 0f)
            var linear_acceleration = floatArrayOf(0f, 0f, 0f)

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

            linear_acceleration[0] = event.values[0] - gravity[0]
            linear_acceleration[1] = event.values[1] - gravity[1]
            linear_acceleration[2] = event.values[2] - gravity[2]

            val x = linear_acceleration[0]
            val y = linear_acceleration[1]
            val z = linear_acceleration[2]

            val value = sqrt(x * x + y * y + z * z)
            if (value > 13) {
                updateSteps()
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    fun fetchSteps() {
        db.collection("BMI").document("mKWvFApbcOYLnbnkW2vc")
            .get()
            .addOnSuccessListener { result ->
                _steps.value = result.data?.values?.elementAt(1).toString().toInt()
            }
            .addOnFailureListener {e ->
                Log.e("MainActivity", "Error Getting data: $e")
            }
    }
    private fun updateSteps(){
        _steps.value = _steps.value!! + 1
        db.collection("BMI").document("mKWvFApbcOYLnbnkW2vc")
            .update("steps", steps.value)
    }
}