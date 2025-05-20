package com.example.appghichu.network.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient1 {
    private const val BASE_URL = "https://newsapi.org/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)  // timeout cho kết nối
        .readTimeout(60, TimeUnit.SECONDS)     // timeout khi đọc dữ liệu
        .writeTimeout(60, TimeUnit.SECONDS)    // timeout khi ghi dữ liệu
        .build()

    val instance: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(NewsApiService::class.java)
    }

    val instance1: NewsBussiness by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(NewsBussiness::class.java)
    }
}