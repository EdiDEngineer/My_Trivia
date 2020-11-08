package edu.utap.mytrivia.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.utap.mytrivia.data.firebase.FirebaseUserAuthLiveData

class MainViewModel : ViewModel() {

    val firebaseUserAuthLiveData = FirebaseUserAuthLiveData()
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    fun load() {
        _isLoading.postValue(true)
    }

    fun resetLoad() {
        _isLoading.postValue(false)
    }
}