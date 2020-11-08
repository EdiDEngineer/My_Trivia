package edu.utap.mytrivia.data.local.entity

import android.text.SpannableString
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "quiz_table")
data class Quiz(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var score: Int = 0,
    val noOfQuestions: String,
    val category: SpannableString,
    val type: String,
    val difficulty: String,
    var rating: String? = null,
    var quizDuration: String = "",
    var timeOfDay: Date = Date(),
    var maxScore:Int =0
)