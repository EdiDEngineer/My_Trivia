package edu.utap.mytrivia.ui.main.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.utap.mytrivia.data.Repository
import edu.utap.mytrivia.data.firebase.FirebaseFireStore
import edu.utap.mytrivia.data.firebase.FirebaseUserAuthLiveData
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(private val repository: Repository) : ViewModel() {

    val firebaseUserAuthLiveData = FirebaseUserAuthLiveData()
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _doneDownLoading = MutableLiveData("")
    val doneDownLoading: LiveData<String> = _doneDownLoading

    fun load() {
        _isLoading.postValue(true)
    }

    fun resetLoad() {
        _isLoading.postValue(false)
    }

    fun downloadQuizzes() {
        viewModelScope.launch {
            val userAuth = firebaseUserAuthLiveData.value
            userAuth?.uid?.let { uid ->
                FirebaseFireStore.download(uid)?.let {
                    repository.insertQuizzes(it)
                }
            }
            userAuth?.email?.let {
                _doneDownLoading.postValue(it)
            }
        }
    }

}