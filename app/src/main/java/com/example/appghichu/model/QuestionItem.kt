package com.example.appghichu.model

data class QuestionItem(
    var type: String,
    var difficulty: String,
    var category: String,
    var question: String,
    var correct_answer: String
) {
}