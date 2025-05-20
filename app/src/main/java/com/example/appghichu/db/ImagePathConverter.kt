package com.example.appghichu.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImagePathConverter {
    @TypeConverter
    fun fromList(imagePaths: List<String>): String {
        return Gson().toJson(imagePaths)
    }

    @TypeConverter
    fun toList(imagePathsString: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(imagePathsString, type)
    }
}