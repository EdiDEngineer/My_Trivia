package edu.utap.mytrivia.data.remote.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TriviaQuestion(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: MutableList<String>
)

@JsonClass(generateAdapter = true)
data class TriviaResponse(val results: List<TriviaQuestion>)
