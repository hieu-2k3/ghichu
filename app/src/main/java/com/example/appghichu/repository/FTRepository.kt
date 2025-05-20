package com.example.appghichu.repository

import android.util.Log
import com.example.appghichu.model.BodyRequestV2
import com.example.appghichu.network.api.FTApi
import com.example.appghichu.network.api.SafeApiRequest
import com.example.appghichu.utils.RequestBodyUtil
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FTRepository(private val api: FTApi) : SafeApiRequest() {
    suspend fun noteChat(path: String,body: BodyRequestV2) = //makeApiRequest {
        api.noteChat(
            signature = RequestBodyUtil.toRequestBodyString(body.signature),
            message = RequestBodyUtil.toRequestBodyString(Gson().toJson(body.message)),
            model = RequestBodyUtil.toRequestBodyString(body.model),
            service = RequestBodyUtil.toRequestBodyString(body.service),
            app = RequestBodyUtil.toRequestBodyString(body.app),
            file = null
        )
    //}
}