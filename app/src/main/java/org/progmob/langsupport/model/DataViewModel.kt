package org.progmob.langsupport.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataViewModel: ViewModel() {
    private val repo = FirebaseRepository
    val loadedWords: MutableLiveData<MutableList<WordData>> = MutableLiveData(mutableListOf())
    val languages: MutableLiveData<Map<String, String>> = MutableLiveData(mapOf())
    val currUser: MutableLiveData<FirebaseUser> = MutableLiveData()

    init {
        repo.currUser.observeForever { currUser.value = it }
        repo.lastAddedWord.observeForever {
            loadedWords.value = newListFromCurrent(loadedWords.value!!).apply { add(it) }
        }
        repo.languages.observeForever { languages.value = it }

    }

    fun signUpUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.signUpUser(email, password)
        }
    }

    fun signInUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.signInUser(email, password)
        }
    }

    fun signOutUser() {
        repo.signOutUser()
    }


    fun fetchLanguages() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.fetchLanguages()
        }
    }


    private fun <T> newListFromCurrent(currList: MutableList<T>): MutableList<T> {
        val newL: MutableList<T> = mutableListOf()
        for(el in currList) { newL.add(el) }
        return newL
    }
}