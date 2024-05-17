package rma.lv1.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import rma.lv1.viewmodel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()){
    val data by viewModel.data.observeAsState()

    LaunchedEffect(true){
        viewModel.fetchData()
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = data?.name ?: "Error",
            fontSize = 55.sp,
            lineHeight = 61.sp,
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Temperature: " + data?.main?.temp.toString()+"°C",
            fontSize = 48.sp,
            lineHeight = 55.sp,
            modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Wind speed: "+ data?.wind?.speed.toString()+"m/s",
            fontSize = 48.sp,
            lineHeight = 55.sp,
            modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Pressure: "+ data?.main?.pressure.toString()+"hpa",
            fontSize = 48.sp,
            lineHeight = 55.sp,
            modifier = Modifier.padding(10.dp)
        )

    }
}