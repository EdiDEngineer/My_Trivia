package edu.utap.mytrivia.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.utap.mytrivia.data.local.entity.Quiz

@Dao
interface QuizDao {

    @Query("SELECT * FROM quiz_table where difficulty =:difficulty order by score desc, timeOfDay desc")
    fun observeQuizzesByDifficulty(difficulty: String): LiveData<List<Quiz>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz)

    @Query("DELETE FROM quiz_table WHERE id = :id")
    suspend fun deleteQuizById(id: Long): Int

    @Query("DELETE FROM quiz_table  where difficulty =:difficulty")
    suspend fun deleteQuizzesByDifficulty(difficulty: String)

}
