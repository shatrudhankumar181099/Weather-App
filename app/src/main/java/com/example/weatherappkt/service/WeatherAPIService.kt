package com.example.weatherappkt.service

import com.example.weatherappkt.model.WeatherModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

//https://api.openweathermap.org/data/2.5/weather?lat=20&lon=77&appid=be91700370aca4cdfe4efee07d223e9b
class WeatherAPIService {
    private val BASE_URL = "https://api.openweathermap.org/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherPI::class.java)

    fun getDataService(cityName:String): Single<WeatherModel>{
        return api.getData(cityName)
    }
}