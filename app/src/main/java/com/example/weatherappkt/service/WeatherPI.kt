package com.example.weatherappkt.service

import com.example.weatherappkt.model.WeatherModel
import retrofit2.http.GET
import io.reactivex.Single
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?lat=20&lon=77&appid=be91700370aca4cdfe4efee07d223e9b

interface WeatherPI {
    @GET("data/2.5/weather?&units=metric&appid=be91700370aca4cdfe4efee07d223e9b")
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>

}