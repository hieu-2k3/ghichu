package com.example.appghichu.network.api

import com.example.appghichu.model.ApiResponse
import com.example.appghichu.model.Question
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part

interface QuestionApiService {
    companion object {
        private fun endPointApi() : String{
            return "https://opentdb.com/"
        }
        private val client = OkHttpClient.Builder().build()
        operator fun invoke(): QuestionApiService {

            return Retrofit.Builder()
                .baseUrl(endPointApi())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuestionApiService::class.java)
        }
    }

    @GET("api.php?amount=50")
    fun getQuestions(): Call<Question>
}