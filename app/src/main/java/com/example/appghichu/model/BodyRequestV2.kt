package com.example.appghichu.model

import java.io.Serializable

class BodyRequestV2(
    val signature: String = "",
    val model: String = "",
    val message: List<Messages> = listOf(),
    val app: String = "NoteAI",
    val service: String = "newwaylabs",
): Serializable {
}