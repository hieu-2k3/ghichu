package com.example.appghichu.network.api

import com.example.appghichu.model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CallApi {
    @GET("/data/2.5/weather")
    fun getWeather(@Query("q") q : String, @Query("appid") appid : String, @Query("units") units : String) : Call<Weather>
}