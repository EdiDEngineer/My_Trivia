package edu.utap.mytrivia.data

import edu.utap.mytrivia.data.firebase.model.FirebaseQuiz
import edu.utap.mytrivia.data.firebase.model.asQuizModel
import edu.utap.mytrivia.data.local.MyTriviaDatabase
import edu.utap.mytrivia.data.local.entity.Quiz
import edu.utap.mytrivia.data.local.entity.asFirebaseQuizModel
import edu.utap.mytrivia.data.remote.TriviaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: TriviaApi,
    private val db: MyTriviaDatabase
) : Repository {

    override suspend fun getQuestions(
        amount: String, difficulty: String,
        type: String,
        category: String
    ) = withContext(Dispatchers.IO) {
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

    override fun observeQuizzesByDifficulty(difficulty: String) =
        db.quizDao.observeQuizzesByDifficulty(difficulty)

    override suspend fun insertQuiz(quiz: Quiz) = withContext(Dispatchers.IO) {
        db.quizDao.insertQuizzes(quiz)
    }

    override suspend fun deleteQuiz(quiz: Quiz) = withContext(Dispatchers.IO) {
        db.quizDao.deleteQuiz(quiz)
    }

    override suspend fun deleteQuizzesByDifficulty(difficulty: String) =
        withContext(Dispatchers.IO) {
            db.quizDao.deleteQuizzesByDifficulty(difficulty)
        }

    override suspend fun clearTables() = withContext(Dispatchers.IO) {
        db.clearAllTables()
    }

    override suspend fun getQuizzesNotUploaded(ownerUid: String) = withContext(Dispatchers.IO) {
        db.quizDao.getQuizzesNotUploaded().asFirebaseQuizModel(ownerUid)
    }

    override suspend fun insertQuizzes(quizzes: List<FirebaseQuiz>) = withContext(Dispatchers.IO) {
        db.quizDao.insertQuizzes(*quizzes.asQuizModel())
    }

    override suspend fun updateQuizzes(quizzes: FirebaseQuiz) = withContext(Dispatchers.IO) {
        db.quizDao.updateQuizzes(quizzes.asQuizModel())
    }

}