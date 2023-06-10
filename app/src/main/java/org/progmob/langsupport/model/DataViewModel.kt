package org.progmob.langsupport.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.progmob.langsupport.util.LanguageManager
import java.lang.Exception
import java.util.Locale

class DataViewModel(application: Application): AndroidViewModel(application) {
    private val firebase = FirebaseRepository
    private val translator = TranslatorRepository
    private val room = RoomRepository
    val activeWords: MutableLiveData<List<WordData>> = MutableLiveData(mutableListOf())
    val activeFavWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val historyWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val translatedWord: MutableLiveData<String?> = MutableLiveData()

    val currUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val errorMsg: MutableLiveData<String> = MutableLiveData()
    val statsData: MutableLiveData<StatsData> = MutableLiveData()

    init {
        setTranslators()
        room.initDatabase(application.applicationContext)
        firebase.currUser.observeForever { currUser.value = it.also { getHistoryAndFavWords() } }

        room.lastAddedWord.observeForever {
            activeWords.value = newListFromCurrent(activeWords.value!!, it)
        }

        room.activeWords.observeForever { activeWords.value = it }
        room.activeFavWords.observeForever { activeFavWords.value = it }
        room.historyWords.observeForever { historyWords.value = it }

        room.currentStats.observeForever { statsData.value = it }
        translator.translatorResult.observeForever { translatedWord.value = it }
    }

    fun signUpUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firebase.signUpUser(email, password)
            } catch (e: Exception) {
                errorMsg.postValue(e.message)
            }
        }
    }

    fun signInUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firebase.signInUser(email, password)
            } catch (e: Exception) {
                errorMsg.postValue(e.message)
            }
        }
    }

    fun signOutUser() {
        firebase.signOutUser()
    }

    fun isUserSignedIn(): Boolean {
        return firebase.getCurrentUser() != null
    }


    fun getWordsLike(s: CharSequence?) {
        viewModelScope.launch(Dispatchers.IO) {
            room.getWordsLike(s)
        }
    }

    fun getFavWordsLike(s: CharSequence?) {
        viewModelScope.launch(Dispatchers.IO) {
            room.getFavWordsLike(s.toString())
        }
    }

    fun addNewWord(newWord: WordData) {
        viewModelScope.launch(Dispatchers.IO) {
            room.addNewWord(newWord)
        }
    }
    fun addWordMeaning(word: WordData, newMeaning: String) {
        viewModelScope.launch(Dispatchers.IO) {
            room.addWordMeaning(word, newMeaning)
        }
    }

    fun translateWord(word: String, lang: String) {
        viewModelScope.launch(Dispatchers.IO) {
            translator.translateWord(word, lang)
        }
    }

    fun updateSearchedWord(word: WordData, guessed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            room.updateSearchedWord(word, guessed)
        }
    }

    fun getStatsData(){
        viewModelScope.launch(Dispatchers.IO) {
            room.getStatsData()
        }
    }

    fun updateFavouriteWord(word: WordData) {
        viewModelScope.launch(Dispatchers.IO) {
            room.updateFavouriteWord(word)
        }
    }

    fun deleteWord(word: WordData){
        viewModelScope.launch(Dispatchers.IO) {
            room.deleteWord(word)
        }
    }


    private fun setTranslators() {
        for(tr in LanguageManager.getLanguages()) {
            viewModelScope.launch(Dispatchers.IO) {
                translator.setNewTranslator(Locale.getDefault().language, tr)
            }
        }
    }

    private fun getHistoryAndFavWords() {
        if(!isUserSignedIn()) return
        viewModelScope.launch(Dispatchers.IO) {
            room.getHistoryWords()
            room.getFavWordsLike("")
        }
    }

    private fun <T> newListFromCurrent(currList: List<T>, newEl: T): List<T> {
        val newL: MutableList<T> = mutableListOf()
        for(el in currList) { newL.add(el) }
        return newL.apply { add(newEl) }.toList()
    }

    override fun onCleared() {
        super.onCleared()
        translator.closeTranslators()
    }
}