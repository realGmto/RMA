package rma.lv1

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
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import rma.lv1.ui.theme.LV1Theme
import java.security.AllPermission

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
                    BackgroundImage(modifier = Modifier)
                }
            }
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
        UserPreview(name = "Nikola", modifier = Modifier.fillMaxSize())
    }

}
@Preview(showBackground = false)
@Composable
fun UserPreview() {
    LV1Theme {
        BackgroundImage(modifier = Modifier)   }
}