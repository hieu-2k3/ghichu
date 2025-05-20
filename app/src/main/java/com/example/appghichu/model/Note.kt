package com.example.appghichu.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.appghichu.db.ImagePathConverter
import java.io.Serializable


@Entity(tableName = "note_table")
@TypeConverters(ImagePathConverter::class)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val title: String?,
    val content: String?,
    val date: String?,
    val color: Int?,
    val imagePaths: List<String>?
): Serializable