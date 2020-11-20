package edu.utap.mytrivia.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.utap.mytrivia.data.local.entity.Quiz

@Dao
interface QuizDao {

    @Query("SELECT * FROM quiz_table where difficulty =:difficulty order by score desc, timeOfDay desc")
    fun observeQuizzesByDifficulty(difficulty: String): LiveData<List<Quiz>>

    @Delete
    suspend fun deleteQuiz(vararg quiz: Quiz)

    @Query("DELETE FROM quiz_table  where difficulty =:difficulty")
    suspend fun deleteQuizzesByDifficulty(difficulty: String)

    @Query("SELECT * FROM quiz_table where upload = 0")
    suspend fun getQuizzesNotUploaded(): List<Quiz>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizzes(vararg quizzes: Quiz)

    @Update
    suspend fun updateQuizzes(vararg quizzes: Quiz)

}
