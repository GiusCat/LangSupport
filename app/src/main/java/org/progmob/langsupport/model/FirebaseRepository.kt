package org.progmob.langsupport.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Locale

object FirebaseRepository {
    private val fb = Firebase
    val currUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val lastAddedWord: MutableLiveData<WordData> = MutableLiveData()
    val activeWords: MutableLiveData<MutableList<WordData>> = MutableLiveData()
    val historyWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val languages: MutableLiveData<List<DocumentReference>> = MutableLiveData()

    init {
        fb.auth.addAuthStateListener { currUser.value = it.currentUser }
    }

    fun getCurrentUser(): FirebaseUser? = fb.auth.currentUser

    suspend fun signUpUser(email: String, password: String) {
        fb.auth.createUserWithEmailAndPassword(email, password).await()
        val mainLang = fb.firestore.collection("languages")
            .document(Locale.getDefault().language)
        fb.firestore.collection("users")
            .document(currUser.value!!.uid)
            .set(UserData("New user", mainLang)).await()
    }

    suspend fun signInUser(email: String, password: String) {
        fb.auth.signInWithEmailAndPassword(email, password).await()
    }

    fun signOutUser() {
        fb.auth.signOut()
    }


    suspend fun fetchLanguages() {
        try {
            val langList = fb.firestore.collection("languages").get().await()
            languages.postValue(langList.documents.map { doc -> doc.reference })
        } catch (e: Exception) {
            Log.e("FBRepository", "${e.message}")
        }
    }

    suspend fun fetchWords(s: CharSequence?) {
        if(s.isNullOrEmpty() || getCurrentUser() == null) {
            activeWords.postValue(mutableListOf())
            return
        }

        val search = s.toString().lowercase()
        val wordList = fb.firestore.collection("users/${getCurrentUser()!!.uid}/words")
            .orderBy("wordIndex")
            .startAt(search)
            .endAt(search + "\uf8ff")
            .get().await()

        activeWords.postValue(wordList.map { it.toObject<WordData>() }.toMutableList())
    }

    suspend fun setNewWord(newWord: WordData) {
        fb.firestore.collection("users/${fb.auth.currentUser!!.uid}/words")
            .document(newWord.word)
            .set(newWord).await()

        lastAddedWord.postValue(newWord)
    }

    suspend fun updateSearchedWord(wordData: WordData, guessed: Boolean) {
        wordData.searched++
        wordData.guessed += if(guessed) 1 else 0
        fb.firestore.collection("users/${getCurrentUser()!!.uid}/words")
            .document(wordData.word)
            .update(mapOf<String,Any>(
                "searched" to wordData.searched,
                "guessed" to wordData.guessed,
                "timestamp" to FieldValue.serverTimestamp()
            )).await()
        updateHistoryWords(wordData)
    }

    suspend fun fetchHistoryWords() {
        getCurrentUser() ?: return
        val histWords = fb.firestore.collection("users/${getCurrentUser()!!.uid}/words")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get().await()
        historyWords.postValue(histWords.take(3).map { it.toObject() })
    }


    private fun updateHistoryWords(word: WordData) {
        val newL = mutableListOf(word).apply { addAll(historyWords.value!!.take(2).filter { it != word }) }
        historyWords.postValue(newL.toList())
    }
}