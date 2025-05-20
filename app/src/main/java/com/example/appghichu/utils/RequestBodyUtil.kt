package com.example.appghichu.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object RequestBodyUtil {
    fun toRequestBodyString(value: String?): RequestBody? {
        return value?.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}