package com.example.appghichu.network.api

import android.annotation.SuppressLint
import android.util.Log
import com.example.appghichu.configs.Config
import com.example.appghichu.model.DirectChat
import com.example.appghichu.utils.RequestBodyUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

interface FTApi {
    companion object {

        @SuppressLint("SuspiciousIndentation")
        operator fun invoke(): FTApi {

            val builder = OkHttpClient.Builder()
            builder.callTimeout(60, TimeUnit.SECONDS)
            builder.connectTimeout(60, TimeUnit.SECONDS)
            builder.readTimeout(60,TimeUnit.SECONDS)
            builder.writeTimeout(60,TimeUnit.SECONDS)

//            val logging = HttpLoggingInterceptor()
//                logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
//                builder.addInterceptor(logging)

            val client = builder.build()
//            Log.e("api", Config.endPointApi())
            val endApi = Config.apiChat()

            return Retrofit.Builder()
                .client(client)
                .baseUrl(endApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FTApi::class.java)
        }
    }

    @Multipart
    @POST("/api/v1.0/direct/message")
    suspend fun noteChat(
        @Part("signature") signature: RequestBody?,
        @Part("message") message: RequestBody?,
        @Part("version_app") version: RequestBody? = RequestBodyUtil.toRequestBodyString("8.2.1"),
        @Part("service") service: RequestBody?,
        @Part("model") model: RequestBody?,
        @Part file: MultipartBody.Part?,
        @Part("app") app: RequestBody?
    ): Response<DirectChat>



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
    ): Call<String>



}