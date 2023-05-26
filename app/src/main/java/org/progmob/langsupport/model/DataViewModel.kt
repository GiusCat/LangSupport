package org.progmob.langsupport.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Locale

class DataViewModel: ViewModel() {
    private val repo = FirebaseRepository
    private val translator = TranslatorRepository
    val loadedWords: MutableLiveData<MutableList<WordData>> = MutableLiveData(mutableListOf())
    val translatedWord: MutableLiveData<String> = MutableLiveData()
    val languages: MutableLiveData<List<DocumentReference>> = MutableLiveData(listOf())
    val currUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val errorMsg: MutableLiveData<String> = MutableLiveData()

    init {
        fetchLanguages()
        repo.currUser.observeForever { currUser.value = it }
        repo.lastAddedWord.observeForever {
            loadedWords.value = newListFromCurrent(loadedWords.value!!).apply { add(it) }
        }
        repo.languages.observeForever { languages.value = it.also { setTranslators(it) } }
        repo.lastSearchedWords.observeForever { loadedWords.value = it }
        translator.translatorResult.observeForever { translatedWord.value = it }
    }

    fun signUpUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.signUpUser(email, password)
            } catch (e: Exception) {
                errorMsg.postValue(e.message)
            }
        }
    }

    fun signInUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.signInUser(email, password)
            } catch (e: Exception) {
                errorMsg.postValue(e.message)
            }
        }
    }

    fun signOutUser() {
        repo.signOutUser()
    }

    fun isUserSignedIn(): Boolean {
        return repo.getCurrentUser() != null
    }


    fun fetchWords(s: CharSequence?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.fetchWords(s)
        }
    }

    fun setNewWord(newWord: WordData) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.setNewWord(newWord)
        }
    }

    fun translateWord(word: String, lang: String) {
        viewModelScope.launch(Dispatchers.IO) {
            translator.translateWord(word, lang)
        }
    }


    private fun fetchLanguages() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.fetchLanguages()
        }
    }

    private fun setTranslators(userData: List<DocumentReference>) {
        for(tr in userData) {
            viewModelScope.launch(Dispatchers.IO) {
                translator.setNewTranslator(Locale.getDefault().language, tr.id)
            }
        }
    }

    private fun <T> newListFromCurrent(currList: MutableList<T>): MutableList<T> {
        val newL: MutableList<T> = mutableListOf()
        for(el in currList) { newL.add(el) }
        return newL
    }

    override fun onCleared() {
        super.onCleared()
        translator.closeTranslators()
    }
}