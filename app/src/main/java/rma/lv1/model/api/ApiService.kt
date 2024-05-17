package rma.lv1.model.api

import retrofit2.Response
import retrofit2.http.GET
import rma.lv1.model.Weather
import rma.lv1.model.WeatherResponse

interface ApiService {
    @GET("data/2.5/weather?lat=45.5511&lon=18.6939&appid=3ed30dfd3f3b7c29429c7b1715751093&units=metric")
    suspend fun getYourData(): Response<WeatherResponse>
}