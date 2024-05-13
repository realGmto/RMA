package rma.lv1.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import rma.lv1.viewmodel.BMIViewModel

@Composable
fun MainScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment =
        Alignment.Center
    ) {
        BackgroundImage(modifier = Modifier.fillMaxSize())
        BMICalculatorScreen(BMIViewModel())
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