package edu.utap.mytrivia.ui.home.fragment.dashboard.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import edu.utap.mytrivia.data.Repository
import edu.utap.mytrivia.data.local.MyTriviaDatabase
import edu.utap.mytrivia.data.local.entity.Quiz
import edu.utap.mytrivia.util.searchSpan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizDashboardViewModel(app: Application) : AndroidViewModel(app) {

    private lateinit var quizzes: List<Quiz>
    private val _getQuizzesByDifficulty = MediatorLiveData<List<Quiz>>()
    val getQuizzesByDifficulty: LiveData<List<Quiz>> = _getQuizzesByDifficulty
    private val repository =
        Repository(db = MyTriviaDatabase.getDatabaseInstance(app))

    fun getQuizzesByDifficulty(difficulty: String) {
        _getQuizzesByDifficulty.apply {
            addSource(repository.observeQuizzesByDifficulty(difficulty)) {
                quizzes = it
                postValue(quizzes)
            }
        }
    }

    fun search(searchString: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _getQuizzesByDifficulty.postValue(quizzes.filter {
                it.score.toString()
                    .startsWith(searchString, true)
                        || it.category.searchSpan(searchString)
            })
        }
    }

    fun deleteQuizById(id: Long) {
        viewModelScope.launch {
            repository.deleteQuizById(id)
        }
    }

    fun deleteQuizzesByDifficulty(difficulty: String) {
        viewModelScope.launch {
            repository.deleteQuizzesByDifficulty(difficulty)
        }
    }

    fun typeFilter(filter: String?) {
        viewModelScope.launch(Dispatchers.Default) {
            if (filter == null) {
                _getQuizzesByDifficulty.postValue(quizzes)
            } else {
                _getQuizzesByDifficulty.postValue(quizzes.filter {
                    it.type == filter
                })
            }
        }
    }

}