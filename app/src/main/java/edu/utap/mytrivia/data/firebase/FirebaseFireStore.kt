package edu.utap.mytrivia.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import edu.utap.mytrivia.data.firebase.entity.FirebaseQuiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseFireStore {
    private val db = FirebaseFirestore.getInstance()
    private const val remoteQuizTable = "remoteQuizTable"

    suspend fun upload(firebase: FirebaseQuiz) = withContext(Dispatchers.IO) {
        try {
            firebase.remoteReferenceID = db.collection(remoteQuizTable).document().id
            db.collection(remoteQuizTable)
                .document(firebase.remoteReferenceID)
                .set(firebase)
                .await()
            firebase
        } catch (e: Exception) {
            null
        }

    }

    suspend fun download(ownerUid: String) = withContext(Dispatchers.IO) {
        try {
            db.collection(remoteQuizTable)
                .whereEqualTo("ownerUid", ownerUid)
                .get()
                .await().documents.mapNotNull {
                    it.toObject(FirebaseQuiz::class.java)
                }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun delete(remoteReferenceID: String) = withContext(Dispatchers.IO) {
        try {
            db.collection(remoteQuizTable)
                .document(remoteReferenceID)
                .delete().await()
        } catch (e: Exception) {
            null
        }
    }

}