package org.progmob.langsupport.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Locale

object FirebaseRepository {
    private val fb = Firebase
    val currUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val lastAddedWord: MutableLiveData<WordData> = MutableLiveData()
    val lastSearchedWords: MutableLiveData<MutableList<WordData>> = MutableLiveData()
    val languages: MutableLiveData<List<DocumentReference>> = MutableLiveData()

    init {
        fb.auth.addAuthStateListener { currUser.value = it.currentUser }
    }

    fun getCurrentUser(): FirebaseUser? = fb.auth.currentUser

    suspend fun setNewWord(newWord: WordData) {
        fb.firestore.collection("users/${fb.auth.currentUser!!.uid}/words")
            .document(newWord.word)
            .set(newWord).await()

        lastAddedWord.value = newWord
        fb.firestore.collection("users")
            .document(fb.auth.currentUser!!.uid)
            .update("knownLanguages", FieldValue.arrayUnion(newWord.lang)).await()
    }

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

    suspend fun fetchLanguages() {
        val langList = fb.firestore.collection("languages").get().await()
        languages.postValue(langList.documents.map { doc -> doc.reference })
    }

    suspend fun fetchWords(s: CharSequence?) {
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

        lastSearchedWords.postValue(wordList.map { it.toObject<WordData>() }.toMutableList())
    }

    fun signOutUser() {
        fb.auth.signOut()
    }
}