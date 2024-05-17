package rma.lv1.model

import retrofit2.Response
import rma.lv1.model.api.RetrofitInstance

class WeatherRepository {
    private val apiService = RetrofitInstance.api

    suspend fun getYourData(): Response<WeatherResponse> {
        return apiService.getYourData()
    }
}