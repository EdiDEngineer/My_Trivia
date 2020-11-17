package edu.utap.mytrivia.ui.main.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import edu.utap.mytrivia.data.Repository
import edu.utap.mytrivia.data.firebase.FirebaseFireStore
import edu.utap.mytrivia.data.firebase.FirebaseUserAuthLiveData
import edu.utap.mytrivia.data.local.MyTriviaDatabase
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    val firebaseUserAuthLiveData = FirebaseUserAuthLiveData()
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val repository =
        Repository(db = MyTriviaDatabase.getDatabaseInstance(app))


    fun load() {
        _isLoading.postValue(true)
    }

    fun resetLoad() {
        _isLoading.postValue(false)
    }

    fun downloadQuizzes() {
        viewModelScope.launch {
            firebaseUserAuthLiveData.uid()?.let { uid ->
                FirebaseFireStore.download(uid)?.let {
                    repository.insertQuizzes(it)
                }
            }
        }
    }

}