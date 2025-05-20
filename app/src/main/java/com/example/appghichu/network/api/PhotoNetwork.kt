package com.example.appghichu.network.api

import com.example.appghichu.model.Data
import com.example.appghichu.model.DataArt
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

interface PhotoNetwork {
    companion object {
        private fun endPointApi() : String{
            return "http://imageai.sboomtools.net"
        }

        operator fun invoke(): PhotoNetwork {
            val client = OkHttpClient
                .Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()


            return Retrofit.Builder()
                .client(client)
                .baseUrl(endPointApi())
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PhotoNetwork::class.java)
        }
    }

    //cancel create art


//    //get all
    @GET("api/v3/home")
    fun getAllData(@Query("page") page: Int): Call<Data>

    @Multipart
    @POST("api/v5/art")
    fun createArt(@Header("signature") signature: String? = null,
                  @Part("prompt") prompt: RequestBody? = null,
                  @Part("ratio") ratio: RequestBody? = null,
                  @Part("style") style: RequestBody? = null,
                  @Part("seed_id") seed_id: RequestBody? = null,
                  @Part image: MultipartBody.Part? = null,
                  @Part("steps") steps: RequestBody? = null,
                  @Part("cfg_scale") cfg_scale: RequestBody? = null,
                  @Part("negative_prompt") negative_prompt: RequestBody? = null): Call<DataArt>

    @Multipart
    @POST("api/sketch")
    fun createSketch(@Header("signature") signature: String? = null,
                     @Part("prompt") prompt: RequestBody? = null,
                     @Part("ratio") ratio: RequestBody? = null,
                     @Part("style") style: RequestBody? = null,
                     @Part("seed_id") seed_id: RequestBody? = null,
                     @Part image: MultipartBody.Part? = null,
                     @Part("steps") steps: RequestBody? = null,
                     @Part("cfg_scale") cfg_scale: RequestBody? = null,
                     @Part("negative_prompt") negative_prompt: RequestBody? = null): Call<DataArt>

    @POST("api/art/{id}/fetch")
    fun fetchArt (@Path("id") artId: Int): Call<DataArt>

//    @Multipart
//    @POST
//    fun imageToText(
//        @Url url:String,
//        @Part image: MultipartBody.Part? = null,
//        @Part("prompt_mode") prompt_mode: RequestBody? = null,
//        @Part("output_mode") output_mode: RequestBody? = null,
//        @Part("max_filename_len") max_filename_len: RequestBody? = null
//    ): Call<TextGenerated>

//    @GET("api/blogs")
//    fun getBlog(@Query("page") page: Int): Call<Blog>
//
//    @GET("api/blog/{id}")
//    fun getBlogDetail(@Path("id") id: Int): Call<BlogDetail>
}

class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val delegate: Converter<ResponseBody, *> =
            retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return Converter { body -> if (body.contentLength() == 0L) null else delegate.convert(body) }
    }
}