package com.example.appghichu.model

class DirectChat(
    val code : Int = 0,
    val message: String = "",
    val data : Messages = Messages()
)

data class ApiResponse(
    val code: Int,
    val message: String,
    val data: ResponseData
)

data class ResponseData(
    val content: String,
    val role: String
)
