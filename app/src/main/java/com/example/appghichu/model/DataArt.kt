package com.example.appghichu.model

import java.io.Serializable

class DataArt(
    val code: Int = 0,
    val status: String = "",
    var art: Art = Art(id = 0,
        image = "",
        prompt = "",
        seed_id = 0,
        style = "",
        ratio = "",
        styleName = "",
        styleImage = "",
        temp_image = "",
        status = 0,
        haveBannedWord = 0,
        count_down = 0.0)
): Serializable