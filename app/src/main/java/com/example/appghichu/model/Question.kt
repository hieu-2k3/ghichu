package com.example.appghichu.model

data class Question(
    var response_code: Int,
    var results: ArrayList<QuestionItem>
) {
}