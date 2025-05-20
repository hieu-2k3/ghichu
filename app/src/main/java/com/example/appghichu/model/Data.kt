package com.example.appghichu.model

import java.io.Serializable

class Data(
    val code: Int = 0,
    val status: String = "",
    var styles: ArrayList<Style> = arrayListOf(),
    var popular_arts: DataPopulars = DataPopulars()
): Serializable