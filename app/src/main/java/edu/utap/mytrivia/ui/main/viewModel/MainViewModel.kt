package edu.utap.mytrivia.ui.main.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.utap.mytrivia.data.Repository
import edu.utap.mytrivia.data.RepositoryImpl
import edu.utap.mytrivia.data.firebase.FirebaseFireStore
import edu.utap.mytrivia.data.firebase.FirebaseUserAuthLiveData
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(private val repository: Repository) : ViewModel() {

    val firebaseUserAuthLiveData = FirebaseUserAuthLiveData()
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun load() {
        _isLoading.postValue(true)
    }

    fun resetLoad() {
        _isLoading.postValue(false)
    }

    fun downloadQuizzes() {
        viewModelScope.launch {
            firebaseUserAuthLiveData.value?.uid?.let { uid ->
                FirebaseFireStore.download(uid)?.let {
                    repository.insertQuizzes(it)
                }
            }
        }
    }

}