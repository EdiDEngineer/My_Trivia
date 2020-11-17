package edu.utap.mytrivia.ui.home.fragment.dashboard.viewModel

import android.app.Application
import androidx.lifecycle.*
import edu.utap.mytrivia.data.Repository
import edu.utap.mytrivia.data.firebase.FirebaseFireStore
import edu.utap.mytrivia.data.firebase.FirebaseUserAuthLiveData
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
    private val firebaseUserAuthLiveData = FirebaseUserAuthLiveData()
    private val _upload = MutableLiveData(false)
    val upload: LiveData<Boolean> = _upload

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
                        it.category.searchSpan(searchString)
            })
        }
    }

    fun deleteQuizById(quiz: Quiz) {
        viewModelScope.launch {
            repository.deleteQuizById(quiz.id)
            FirebaseFireStore.delete(quiz.remoteReferenceID)
        }
    }

    fun deleteQuizzesByDifficulty(difficulty: String) {
        viewModelScope.launch {
            repository.deleteQuizzesByDifficulty(difficulty)
            _getQuizzesByDifficulty.value?.forEach {
                FirebaseFireStore.delete(it.remoteReferenceID)
            }
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

    fun uploadQuizzes() {
        _upload.postValue(true)
        viewModelScope.launch {
            firebaseUserAuthLiveData.uid()?.let { uid ->
                repository.getQuizzesNotUploaded(uid).forEach{ firebaseQuiz ->
                    FirebaseFireStore.upload(firebaseQuiz)?.let {
                        repository.updateQuizzes(it)
                    }
                }
            }
            _upload.postValue(false)
        }
    }

}