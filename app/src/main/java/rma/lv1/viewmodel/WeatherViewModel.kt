package rma.lv1.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import rma.lv1.model.Weather
import rma.lv1.model.WeatherRepository
import rma.lv1.model.WeatherResponse

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WeatherRepository()

    private val _data = MutableLiveData<WeatherResponse>()
    val data: LiveData<WeatherResponse> get() = _data

    fun fetchData() {
        viewModelScope.launch {
            try {
                val response = repository.getYourData()
                if (response.isSuccessful) {
                    _data.postValue(response.body())
                    Log.e("INFO", _data.value?.name ?: "Error getting data")
                } else {
                    Log.e("ERROR",response.message())
                }
            } catch (e: Exception) {
                Log.e("ERROR",e.toString())
            }
        }
    }
}