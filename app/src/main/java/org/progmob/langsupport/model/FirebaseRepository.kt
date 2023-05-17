package org.progmob.langsupport.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object FirebaseRepository {
    val fb = Firebase
    val currUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val lastAddedWord: MutableLiveData<WordData> = MutableLiveData()
    val lastSearchedWords: MutableLiveData<MutableList<WordData>> = MutableLiveData()
    val languages: MutableLiveData<MutableMap<String, String>> = MutableLiveData()

    init {
        fb.auth.addAuthStateListener { auth ->
            currUser.value = auth.currentUser
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return fb.auth.currentUser
    }

    fun setNewWord(newWord: WordData) {
        fb.firestore.collection("users/${fb.auth.currentUser!!.uid}/words")
            .document(newWord.word)
            .set(newWord)
            // .add(newWord)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    lastAddedWord.value = newWord
                }
            }
    }

    suspend fun signUpUser(email: String, password: String) {
        fb.auth.createUserWithEmailAndPassword(email, password).await()
        val mainLang = fb.firestore.collection("languages").document("it")
        fb.firestore.collection("users").document(currUser.value!!.uid).set(
            hashMapOf(
                "main_lang" to mainLang,
                "name" to "New user"
            )
        ).await()
    }

    suspend fun signInUser(email: String, password: String) {
        fb.auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun fetchLanguages() {
        val langList = fb.firestore.collection("languages").get().await()
        languages.postValue(mutableMapOf<String, String>().apply {
            for(el in langList) { this[el.id] = el.data["description"] as String }
        })
    }

    suspend fun searchWords(s: CharSequence?) {
        if(s.isNullOrEmpty() || getCurrentUser() == null) {
            lastSearchedWords.postValue(mutableListOf())
            return
        }

        val search = s.toString().lowercase()
        val wordList = fb.firestore.collection("users/${getCurrentUser()!!.uid}/words")
            .orderBy("wordIndex")
            .startAt(search)
            .endAt(search + "\uf8ff")
            .get().await()

        // TODO: optimise list update with DiffUtil
        lastSearchedWords.postValue(wordList.map { it.toObject<WordData>() }.toMutableList())
    }

    fun signOutUser() {
        fb.auth.signOut()
    }


}