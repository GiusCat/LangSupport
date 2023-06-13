package org.progmob.langsupport.util

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.FirebaseException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.model.database.WordDatabase

class FirebaseUpdaterWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    private val db = Room.databaseBuilder(appContext, WordDatabase::class.java, "words").build()
    private val tag = this::class.simpleName

    override suspend fun doWork(): Result {
        Log.i(tag, "Worker begin")
        if(Firebase.auth.currentUser == null) {
            Log.i(tag, "Worker exited, user is not signed in")
            return Result.success()
        }
        return try {
            withContext(Dispatchers.IO) {
                setAllUserWords(db.wordDao().getAllWords())
                Log.i(tag, "Worker success")
                Result.success()
            }
        } catch (e: FirebaseException) {
            Log.i(tag, "Worker failure")
            Result.failure()
        }
    }

    private suspend fun setAllUserWords(words: List<WordData>) {
        for(w in words) {
            Firebase.firestore
                .collection("users/${Firebase.auth.currentUser!!.uid}/words")
                .document(w.word)
                .set(w).await()
        }
    }
}