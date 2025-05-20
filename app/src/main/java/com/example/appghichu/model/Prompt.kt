package com.example.appghichu.model

import java.io.Serializable

class Prompt(
    var id:Long = 0,
    var image:String = "",
    var prompt: String = "",
    var style:String = "",
    var ratio:String = "",
): Serializable