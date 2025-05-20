package com.example.appghichu.network.api

import com.example.appghichu.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    fun getAppleNews(
        @Query("q") query: String = "apple",
        @Query("from") from: String = "2025-05-12",
        @Query("to") to: String = "2025-05-12",
        @Query("sortBy") sortBy: String = "popularity",
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>
}

interface NewsBussiness {
    @GET("v2/everything")
    fun getNewsByQuery(
        @Query("q") query: String,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

}