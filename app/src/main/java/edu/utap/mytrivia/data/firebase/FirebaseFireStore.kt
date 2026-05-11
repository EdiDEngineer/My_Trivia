package edu.utap.mytrivia.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import edu.utap.mytrivia.data.firebase.model.FirebaseQuiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

object FirebaseFireStore {
    private const val remoteQuizTable = "remoteQuizTable"

    suspend fun upload(firebase: FirebaseQuiz) = withContext(Dispatchers.IO) {
        try {
            val db =  FirebaseFirestore.getInstance()
            firebase.remoteReferenceID =  db.collection(remoteQuizTable).document().id
            db.collection(remoteQuizTable)
                .document(firebase.remoteReferenceID)
                .set(firebase)
                .await()
            firebase
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    suspend fun download(ownerUid: String) = withContext(Dispatchers.IO) {
        try {
            val db =  FirebaseFirestore.getInstance()
            db.collection(remoteQuizTable)
                .whereEqualTo("ownerUid", ownerUid)
                .get()
                .await().documents.mapNotNull {
                    it.toObject(FirebaseQuiz::class.java)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun delete(remoteReferenceID: String) = withContext(Dispatchers.IO) {
        try {
            val db =  FirebaseFirestore.getInstance()
            db.collection(remoteQuizTable)
                .document(remoteReferenceID)
                .delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}