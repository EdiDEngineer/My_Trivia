package edu.utap.mytrivia.data.firebase

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseUserAuthLiveData : LiveData<FirebaseUser?>() {

    private val firebaseAuth = FirebaseAuth.getInstance().apply {
        useAppLanguage()
    }

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        value = firebaseAuth.currentUser
    }

    override fun onActive() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    suspend fun createAccount(email: String, password: String) = withContext(Dispatchers.IO)
    {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    suspend fun signIn(email: String, password: String) =
        withContext(Dispatchers.IO)
        {
            firebaseAuth.signInWithEmailAndPassword(email, password)
        }

    fun signOut() = firebaseAuth.signOut()

}