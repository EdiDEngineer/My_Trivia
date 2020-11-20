package edu.utap.mytrivia.data

import androidx.lifecycle.LiveData
import edu.utap.mytrivia.data.firebase.model.FirebaseQuiz
import edu.utap.mytrivia.data.local.entity.Quiz
import edu.utap.mytrivia.data.remote.model.TriviaQuestion

interface Repository {
    suspend fun getQuestions(
        amount: String, difficulty: String,
        type: String,
        category: String
    ): List<TriviaQuestion>?

    fun observeQuizzesByDifficulty(difficulty: String): LiveData<List<Quiz>>

    suspend fun insertQuiz(quiz: Quiz)
    suspend fun deleteQuiz(quiz: Quiz)

    suspend fun deleteQuizzesByDifficulty(difficulty: String)

    suspend fun clearTables()

    suspend fun getQuizzesNotUploaded(ownerUid: String): List<FirebaseQuiz>

    suspend fun insertQuizzes(quizzes: List<FirebaseQuiz>)

    suspend fun updateQuizzes(quizzes: FirebaseQuiz)
}