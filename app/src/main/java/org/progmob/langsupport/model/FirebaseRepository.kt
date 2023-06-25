package org.progmob.langsupport.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Locale

object FirebaseRepository {
    private val fb = Firebase
    val currUser: MutableLiveData<FirebaseUser> = MutableLiveData()

    fun initFirebase(firstRun: Boolean) {
        if(firstRun)
            fb.auth.signOut()

        fb.auth.addAuthStateListener {
            if(it.currentUser?.email != currUser.value?.email)
                currUser.value = it.currentUser
        }
    }

    fun getCurrentUser(): FirebaseUser? = fb.auth.currentUser

    suspend fun signUpUser(email: String, password: String) {
        fb.auth.createUserWithEmailAndPassword(email, password).await()
        fb.firestore.collection("users")
            .document(currUser.value!!.uid)
            .set(hashMapOf(
                "name" to "New user",
                "mainLang" to Locale.getDefault().language)).await()
    }

    suspend fun signInUser(email: String, password: String) {
        fb.auth.signInWithEmailAndPassword(email, password).await()
    }

    fun signOutUser() {
        fb.auth.signOut()
    }

    suspend fun fetchAllUserWords(): List<WordData> {
        if(fb.auth.currentUser == null) return listOf()
        val a = fb.firestore.collection("users/${fb.auth.currentUser!!.uid}/words")
            .get().await()
        return a.map { it.toObject() }
    }
}