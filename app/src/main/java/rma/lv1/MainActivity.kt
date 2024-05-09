package rma.lv1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import rma.lv1.ui.theme.LV1Theme
import java.security.AllPermission
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.TriggerEvent
import android.hardware.TriggerEventListener
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LV1Theme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "main_screen") {
                        composable("main_screen") {
                            MainScreen(navController = navController)
                        }
                        composable("step_counter") {
                            StepCounter(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

    @Composable
    fun MainScreen(navController: NavController) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment =
            Alignment.Center
        ) {
            BackgroundImage(modifier = Modifier.fillMaxSize())
            UserPreview("Nikola")
            // Button to navigate to StepCounter
            Button(
                onClick = {
                    // Navigate to OtherScreen when button clicked
                    navController.navigate("step_counter")
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text(text = "Step Counter")
            }
        }
    }
    @Composable
    fun StepCounter(navController: NavController) {
        val db = Firebase.firestore
        var steps by remember {
            mutableStateOf(0)
        }

        db.collection("BMI").document("mKWvFApbcOYLnbnkW2vc")
            .get()
            .addOnSuccessListener { result ->
                steps = result.getDouble("steps")!!.toInt()
            }
            .addOnFailureListener {e ->
                Log.e("MainActivity", "Error Getting data: $e")
            }

        val sensorManager =
            (LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as
                    SensorManager)
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


        val triggerEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
                    if(event?.values == null) {
                        return
                    }
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
                        steps++
                        db.collection("BMI").document("mKWvFApbcOYLnbnkW2vc")
                            .update("steps", steps)
                    }
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                // Do nothing
            }
        }
        sensor?.also { light ->
            sensorManager.registerListener(triggerEventListener, light, SensorManager.SENSOR_DELAY_NORMAL)
        }




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


@Composable
fun UserPreview(name: String, modifier: Modifier = Modifier) {

    val db = Firebase.firestore
    var weight by remember {
        mutableStateOf(0.0)
    }
    var height by remember {
        mutableStateOf(0.0)
    }
    var bmi by remember {
        mutableStateOf(0.0)
    }
    var newWeight by remember {
        mutableStateOf(0.0)
    }
    var newHeight by remember {
        mutableStateOf(0.0)
    }

    db.collection("BMI").document("mKWvFApbcOYLnbnkW2vc")
        .get()
        .addOnSuccessListener { result ->
            weight = result.getDouble("weight")!!
            height = result.getDouble("height")!!
            bmi = weight / (height * height)
        }
        .addOnFailureListener {e ->
            Log.e("MainActivity", "Error Getting data: $e")
        }


    Text(
        text = "Pozdrav $name!",
        fontSize = 20.sp,
        lineHeight = 56.sp,
        modifier= Modifier
            .padding(top = 8.dp)
            .padding(start = 10.dp)

    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Text(
            text = "Tvoj BMI je:",
            fontSize = 55.sp,
            lineHeight = 61.sp,
            textAlign = TextAlign.Center,


            )
        Text(
            text = String.format("%.2f", bmi),
            fontSize = 70.sp,
            lineHeight = 72.sp,
            fontWeight = FontWeight.Bold,
        )

        TextField(
            value = newWeight.toString(),
            onValueChange = { newWeight = it.toDoubleOrNull() ?: 0.0 },
            label = { Text("Nova Tezina:") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.width(20.dp))

        TextField(
            value = newHeight.toString(),
            onValueChange = { newHeight = it.toDoubleOrNull() ?: 0.0 },
            label = { Text("Nova Visina:") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Button(onClick = {
            val docRef = db.collection("BMI").document("mKWvFApbcOYLnbnkW2vc")
            docRef.update(mapOf(
                "weight" to newWeight,
                "height" to newHeight
            ))
                .addOnSuccessListener {
                    weight = newWeight
                    height = newHeight
                    bmi = weight / (height * height)
                }
                .addOnFailureListener { e ->
                    // Update failed (handle error, e.g., show an error message)
                    Log.e("MainActivity", "Error updating Tezina: $e")
                }
        }) {
            Text(text = "Update")
        }
    }
}
@Composable
fun BackgroundImage(modifier: Modifier) {

    Box (modifier){ Image(
        painter = painterResource(id = R.drawable.fitness),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = 0.1F
    )
    }
}
@Preview(showBackground = false)
@Composable
fun UserPreview() {
    LV1Theme {
        BackgroundImage(modifier = Modifier)   }
}