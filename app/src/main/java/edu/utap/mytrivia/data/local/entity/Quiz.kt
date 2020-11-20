package edu.utap.mytrivia.data.local.entity

import android.text.SpannableString
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import edu.utap.mytrivia.data.firebase.model.FirebaseQuiz
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
    var maxScore: Int = 0,
    var upload: Int= 0,
    val remoteReferenceID:String =""
)

fun List<Quiz>.asFirebaseQuizModel(ownerUid: String) = map {
    FirebaseQuiz(
        id = it.id,
        score = it.score,
        noOfQuestions = it.noOfQuestions,
        category = it.category.toString(),
        type = it.type,
        difficulty = it.difficulty,
        rating = it.rating ?: "",
        quizDuration = it.quizDuration,
        timeOfDay = Timestamp(it.timeOfDay),
        maxScore = it.maxScore,
        ownerUid = ownerUid,
        remoteReferenceID = it.remoteReferenceID
    )
}