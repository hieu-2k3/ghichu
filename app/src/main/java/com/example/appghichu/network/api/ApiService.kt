package com.example.appghichu.network.api

import com.example.appghichu.model.ApiResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

interface ApiService {
    companion object {
        private fun endPointApi() : String{
            return "https://chatopenai.sboomtools.net/api/v1.0/"
        }
        private val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // kết nối tới server
            .readTimeout(60, TimeUnit.SECONDS)    // thời gian chờ để đọc dữ liệu
            .writeTimeout(60, TimeUnit.SECONDS)   // thời gian chờ để ghi dữ liệu
            .build()
        operator fun invoke(): ApiService {

            return Retrofit.Builder()
                .baseUrl(endPointApi())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }


    @Multipart
    @POST("direct/message")
    fun sendMessage(
        @Part("signature") signature: RequestBody,
        @Part("version_app") versionApp: RequestBody,
        @Part("service") service: RequestBody,
        @Part("model") model: RequestBody,
        @Part("app") app: RequestBody,
        @Part("message") message: RequestBody,
        @Part file: MultipartBody.Part?
    ): Call<ApiResponse>
}