package edu.utap.mytrivia.data.firebase.model

import android.text.SpannableString
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import edu.utap.mytrivia.data.local.entity.Quiz
import java.util.*

data class FirebaseQuiz(
    var id: Long = 0L,
    var score: Int = 0,
    var noOfQuestions: String = "",
    var category: String = "",
    var type: String = "",
    var difficulty: String = "",
    var rating: String = "",
    var quizDuration: String = "",
    @ServerTimestamp var timeOfDay: Timestamp? = null,
    var maxScore: Int = 0,
    var ownerUid: String = "",
    var remoteReferenceID:String =""
)

fun List<FirebaseQuiz>.asQuizModel() = map {
    Quiz(
        id = it.id,
        score = it.score,
        noOfQuestions = it.noOfQuestions,
        category = SpannableString(it.category),
        type = it.type,
        difficulty = it.difficulty,
        rating = it.rating,
        quizDuration = it.quizDuration,
        timeOfDay = (it.timeOfDay?.toDate() ?: Date()),
        maxScore = it.maxScore,
        upload = 1,
        remoteReferenceID = it.remoteReferenceID
    )
}.toTypedArray()

fun FirebaseQuiz.asQuizModel() =
    Quiz(
        id = id,
        score = score,
        noOfQuestions = noOfQuestions,
        category = SpannableString(category),
        type = type,
        difficulty = difficulty,
        rating = rating,
        quizDuration = quizDuration,
        timeOfDay = (timeOfDay?.toDate() ?: Date()),
        maxScore = maxScore,
        upload = 1,
        remoteReferenceID = remoteReferenceID
    )

