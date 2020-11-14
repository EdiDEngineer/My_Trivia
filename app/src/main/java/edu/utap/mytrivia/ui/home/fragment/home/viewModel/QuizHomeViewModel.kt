package edu.utap.mytrivia.ui.home.fragment.home.viewModel

import android.app.Application
import android.text.SpannableString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import edu.utap.mytrivia.R
import edu.utap.mytrivia.data.Repository
import edu.utap.mytrivia.data.firebase.FirebaseUserAuthLiveData
import edu.utap.mytrivia.data.local.MyTriviaDatabase
import edu.utap.mytrivia.data.local.entity.Quiz
import edu.utap.mytrivia.data.remote.entity.TriviaQuestion
import edu.utap.mytrivia.util.getDurationString
import kotlinx.coroutines.*
import java.util.*

class QuizHomeViewModel(app: Application) : AndroidViewModel(app) {

    private val anyPoint = 8
    private val easyPoint = 2
    private val mediumPoint = 3
    private val hardPoint = 5
    private val easySeconds = 5
    private val mediumSeconds = 10
    private val anyOrHardSeconds = 15
    private var answered = 0
    private var quiz: Quiz? = null
    private var quizDuration: Int? = null
    private var timerJob: Deferred<Unit>? = null
    private var questions: List<TriviaQuestion>? = null
    val firebaseUserAuthLiveData = FirebaseUserAuthLiveData()
    private val _currentTime = MutableLiveData("")
    private val _question = MutableLiveData<TriviaQuestion?>()
    private val _settingsInvalid = MutableLiveData(false)
    val settingsInvalid: LiveData<Boolean> = _settingsInvalid
    val currentTime: LiveData<String> = _currentTime
    val question: LiveData<TriviaQuestion?> = _question
    private val categories = app.resources.getStringArray(R.array.category)
    private val repository =
        Repository(db = MyTriviaDatabase.getDatabaseInstance(app))

    fun saveValid() = quiz?.rating != null

    fun getScore() = (quiz?.score ?: 0).toString()

    fun getMaxScore() = (quiz?.maxScore ?: 0).toString()

    private fun getTimeForQuiz(noOfQuestions: Int, difficulty: String) = when (difficulty) {
        "easy" -> (noOfQuestions * easySeconds)
        "medium" -> (noOfQuestions * mediumSeconds)
        else -> (noOfQuestions * anyOrHardSeconds)
    }

    private fun getScoreForQuestions(noOfQuestions: Int = 1, difficulty: String) =
        when (difficulty) {
            "easy" -> noOfQuestions * easyPoint
            "medium" -> noOfQuestions * mediumPoint
            "hard" -> noOfQuestions * hardPoint
            else -> noOfQuestions * anyPoint
        }

    private fun getBonusForQuiz(score: Int, duration: Int, difficulty: String) = when (difficulty) {
        "easy" -> (score / easyPoint) * (duration / easySeconds)
        "medium" -> (score / mediumPoint) * (duration / mediumSeconds)
        "hard" -> (score / hardPoint) * (duration / anyOrHardSeconds)
        else -> (score / anyPoint) * (duration / anyOrHardSeconds)
    }

    fun clearTables() {
        viewModelScope.launch {
            repository.clearTables()
        }
    }

    fun getQuestions(
    ) {
        quiz?.let {
            viewModelScope.launch {
                questions = getQuestions(it)
            }
        }
    }

    private suspend fun getQuestions(
        quiz: Quiz
    ) = repository.getQuestions(
        (quiz.noOfQuestions.toInt() - answered).toString(),
        if (quiz.difficulty == "Any") "" else quiz.difficulty.toLowerCase(
            Locale.ROOT
        ),
        when (quiz.type) {
            "Any" -> ""
            "Multiple Choice" -> "multiple"
            else -> "boolean"
        },
        if (quiz.category.toString() == "Any") "" else (categories.indexOf(quiz.category.toString()) + 8).toString()
    )?.apply {
        if (isNotEmpty()) {
            shuffled()
            val quest = first()
            quest.incorrect_answers.add(quest.correct_answer)
            quest.incorrect_answers.shuffled()
            _question.postValue(quest)
            if (quizDuration == null) {
                quizDuration = getTimeForQuiz(quiz.noOfQuestions.toInt(), quiz.difficulty)
                quiz.maxScore = getScoreForQuestions(quiz.noOfQuestions.toInt(), quiz.difficulty)
            }
            startTimer()
        }else{
            _settingsInvalid.postValue(true)
        }
    }


    fun startTimer() {
        stopTimer()
        timerJob = viewModelScope.async(Dispatchers.Default) {
            while (isActive) {
                quizDuration?.let {
                    _currentTime.postValue(it.getDurationString())
                    if (it == 0) {
                        cancel()
                    }
                    delay(1000L)
                    quizDuration = it.minus(1)
                }
            }
        }
        timerJob?.start()
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    fun resetQuiz() {
        _question.postValue(null)
        answered = 0
        quiz = null
        questions = null
        quizDuration = null
        _currentTime.postValue("")
        stopTimer()
        _settingsInvalid.postValue(false)
    }

    fun setQuiz(
        amount: String,
        difficulty: String,
        type: String,
        category: String
    ) {

        quiz = Quiz(
            noOfQuestions = amount,
            category = SpannableString.valueOf(category),
            type = type,
            difficulty = difficulty
        )
        getQuestions()
    }

    fun setAnswer(choice: String): Boolean {
        quiz?.let {
            questions?.let { list ->
                answered += 1
                if (_question.value?.correct_answer == choice) {
                    it.score += getScoreForQuestions(difficulty = it.difficulty)
                }

                if (answered < it.noOfQuestions.toInt()) {
                    _question.value?.let {
                        val quest = list[list.indexOf(it) + 1]
                        quest.incorrect_answers.add(quest.correct_answer)
                        quest.incorrect_answers.shuffled()
                        _question.postValue(quest)
                    }
                } else {
                    quizDuration?.let { duration ->
                        val bonus = getBonusForQuiz(it.score, duration, it.difficulty)
                        it.score += bonus
                        it.maxScore += bonus
                    }
                    return true
                }
            }
        }
        return false
    }

    fun setRating(rating: Float) {
        quiz?.let {
            it.rating = rating.toString()
        }
    }

    fun save() {
        viewModelScope.launch {
            quiz?.let { quiz ->
                quiz.timeOfDay = Date()
                quizDuration?.let {
                    quiz.quizDuration =
                        (getTimeForQuiz(
                            quiz.noOfQuestions.toInt(),
                            quiz.difficulty
                        ) - it).toString()
                }
                repository.insertQuiz(quiz)
            }
        }
    }

}