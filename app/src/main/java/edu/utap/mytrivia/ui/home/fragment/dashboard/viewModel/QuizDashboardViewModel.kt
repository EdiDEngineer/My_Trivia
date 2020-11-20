package edu.utap.mytrivia.ui.home.fragment.dashboard.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import edu.utap.mytrivia.data.Repository
import edu.utap.mytrivia.data.RepositoryImpl
import edu.utap.mytrivia.data.firebase.FirebaseFireStore
import edu.utap.mytrivia.data.firebase.FirebaseUserAuthLiveData
import edu.utap.mytrivia.data.local.entity.Quiz
import edu.utap.mytrivia.util.searchSpan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizDashboardViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private lateinit var quizzes: List<Quiz>
    private val _getQuizzesByDifficulty = MediatorLiveData<List<Quiz>>()
    val getQuizzesByDifficulty: LiveData<List<Quiz>> = _getQuizzesByDifficulty
    val firebaseUserAuthLiveData = FirebaseUserAuthLiveData()
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

    fun deleteQuiz(quiz: Quiz) {
        viewModelScope.launch {
            repository.deleteQuiz(quiz)
            FirebaseFireStore.delete(quiz.remoteReferenceID)
        }
    }

    fun deleteQuizzesByDifficulty(difficulty: String) {
        viewModelScope.launch {
            repository.deleteQuizzesByDifficulty(difficulty)
            quizzes.forEach {
                FirebaseFireStore.delete(it.remoteReferenceID)
            }
        }
    }

    fun typeFilter(filter: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _getQuizzesByDifficulty.postValue(quizzes.filter {
                it.type.startsWith(filter)
            })
        }
    }

    fun uploadQuizzes() {
        _upload.postValue(true)
        viewModelScope.launch {
            firebaseUserAuthLiveData.value?.uid?.let { uid ->
                repository.getQuizzesNotUploaded(uid).forEach { firebaseQuiz ->
                    FirebaseFireStore.upload(firebaseQuiz)?.let {
                        repository.updateQuizzes(it)
                    }
                }
            }
            _upload.postValue(false)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.clearTables()
            firebaseUserAuthLiveData.signOut()
        }
    }

}