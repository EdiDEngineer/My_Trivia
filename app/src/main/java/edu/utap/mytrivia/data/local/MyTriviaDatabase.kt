package edu.utap.mytrivia.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.utap.mytrivia.data.local.converter.Converters
import edu.utap.mytrivia.data.local.dao.QuizDao
import edu.utap.mytrivia.data.local.entity.Quiz

@Database(entities = [Quiz::class], version =1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyTriviaDatabase : RoomDatabase() {

    abstract val quizDao: QuizDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: MyTriviaDatabase
        fun getDatabaseInstance(context: Context): MyTriviaDatabase {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                   INSTANCE = Room.databaseBuilder(
                            context,
                            MyTriviaDatabase::class.java,
                            "My Trivia Database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()
                }
                return INSTANCE
            }
        }
    }
}
