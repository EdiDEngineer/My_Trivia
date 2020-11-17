package edu.utap.mytrivia.data

import edu.utap.mytrivia.data.firebase.model.FirebaseQuiz
import edu.utap.mytrivia.data.firebase.model.asQuizModel
import edu.utap.mytrivia.data.local.MyTriviaDatabase
import edu.utap.mytrivia.data.local.entity.Quiz
import edu.utap.mytrivia.data.local.entity.asFirebaseQuizModel
import edu.utap.mytrivia.data.remote.TriviaApi
import edu.utap.mytrivia.data.remote.model.TriviaQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
    private val api: TriviaApi = TriviaApi.create(),
    private val db: MyTriviaDatabase
) {

    suspend fun getQuestions(
        amount: String, difficulty: String,
        type: String,
        category: String
    ): List<TriviaQuestion>? = withContext(Dispatchers.IO) {
        try {
            api.getQuestions(
                amount, difficulty,
                type,
                category
            ).body()?.results
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun observeQuizzesByDifficulty(difficulty: String) =
        db.quizDao.observeQuizzesByDifficulty(difficulty)

    suspend fun insertQuiz(quiz: Quiz) = withContext(Dispatchers.IO) {
        db.quizDao.insertQuizzes(quiz)
    }

    suspend fun deleteQuizById(id: Long): Int = withContext(Dispatchers.IO) {
        db.quizDao.deleteQuizById(id)
    }

    suspend fun deleteQuizzesByDifficulty(difficulty: String) = withContext(Dispatchers.IO) {
        db.quizDao.deleteQuizzesByDifficulty(difficulty)
    }

    suspend fun clearTables() = withContext(Dispatchers.IO) {
        db.clearAllTables()
    }

    suspend fun getQuizzesNotUploaded(ownerUid: String) = withContext(Dispatchers.IO) {
        db.quizDao.getQuizzesNotUploaded().asFirebaseQuizModel(ownerUid)
    }

    suspend fun insertQuizzes(quizzes: List<FirebaseQuiz>) = withContext(Dispatchers.IO) {
        db.quizDao.insertQuizzes(*quizzes.asQuizModel())
    }

    suspend fun updateQuizzes(quizzes: FirebaseQuiz) = withContext(Dispatchers.IO) {
        db.quizDao.updateQuizzes(quizzes.asQuizModel())
    }

}