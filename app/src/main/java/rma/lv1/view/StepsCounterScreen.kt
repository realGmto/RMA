package rma.lv1.view

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import rma.lv1.viewmodel.StepsViewModel
import kotlin.math.sqrt

@Composable
fun StepCounter(navController: NavController, viewModel: StepsViewModel) {
    val steps by viewModel.steps.observeAsState()

    viewModel.setSensor(LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as
            SensorManager)
    viewModel.registerSensors()
    viewModel.fetchSteps()

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(modifier = Modifier.fillMaxSize())
        Column {
            Text(
                text = "Step Count",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = steps.toString(),
                fontSize = 20.sp
            )
        }
        // Back button
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("User Info")
        }
    }
}