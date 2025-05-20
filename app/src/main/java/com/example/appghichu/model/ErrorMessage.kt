package com.example.appghichu.model

import java.io.Serializable

data class ErrorMessage(
    var message:String = "",
    val type:String=""
): Serializable