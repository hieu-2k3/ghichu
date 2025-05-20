package com.example.appghichu.network.api

import android.util.Log
import com.example.appghichu.model.ErrorMessage
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T : Any> makeApiRequest(work: suspend () -> Response<T>): Any?{
        try {
            val response = work.invoke()
            if (response.isSuccessful) {
                if (response.body() == null) {
                    Log.e("API->", "response null")
                    throw Exception(
                        response.code().toString()
                    )
                } else {
                    Log.e("API->", "response3 returned")
                    Log.e("API->", "response2 raw ${response.raw()}")
                    Log.e("API->", "response4 header ${response.headers()}")
                    Log.e("API->", "response3 returned ${response.message()}")
//                    Log.e("API->", "response5 ${")
                    Log.e("BODY",response.body().toString())
                    return response.body()!!
                }
            } else {
                Log.e("API->", "response Error")
                val error = response.errorBody()?.charStream()?.readText()
                if (error != null) {
                    Log.e("API ERROR BODY = ", error)
                    try {
                        val obj = JSONObject(error)
                        val message = obj.getString("message")
                        Log.e("API ERROR JSON = ", message)
                        // throw Exception(message)
                        return ErrorMessage(error,"")

                    } catch (e: JSONException) {
                        Log.e("JSON ERROR ->", e.localizedMessage)
                        throw e
                    }
                } else {
                    throw Exception(
                        response.code().toString()
                    )
                }
            }
        } catch (e: java.lang.Exception) {
            Log.e("ERROR RESPONSE", e.localizedMessage)
            throw e
        }
    }
}