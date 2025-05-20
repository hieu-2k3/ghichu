package com.example.appghichu.model

import java.io.Serializable

class Style(
    val name:String = "",
    val image:String = "",
    var isSelected:Boolean = false,
    var prompt:String = "",
    var model:String = "",
    var description:String = "",
    var is_vip:Int = 0
): Serializable