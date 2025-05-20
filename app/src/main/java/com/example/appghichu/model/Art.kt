package com.example.appghichu.model

import java.io.Serializable

class Art(
    val id: Int = 0,
    val image: String = "",
    var prompt: String = "",
    var seed_id: Long = 0,
    var style: String = "",
    var ratio: String = "",
    var styleName: String = "",
    var styleImage: String = "",
    var temp_image: String = "",
    val status: Int = 0,
    val haveBannedWord: Int = 0,
    val count_down: Double = 0.0
): Serializable