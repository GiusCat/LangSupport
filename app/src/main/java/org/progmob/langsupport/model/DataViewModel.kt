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
    private val repo = FirebaseRepository
    private val translator = TranslatorRepository
    private val room = RoomRepository
    val activeWords: MutableLiveData<List<WordData>> = MutableLiveData(mutableListOf())
    val historyWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val translatedWord: MutableLiveData<String> = MutableLiveData()
    val currUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val errorMsg: MutableLiveData<String> = MutableLiveData()
    val statsData: MutableLiveData<StatsData> = MutableLiveData()

    init {
        setTranslators()
        room.initDatabase(application.applicationContext)

        room.activeWords.observeForever { activeWords.value = it }
        repo.currUser.observeForever { currUser.value = it.also { fetchHistoryWords() } }

        //repo.lastAddedWord.observeForever {
        room.lastAddedWord.observeForever {
            activeWords.value = newListFromCurrent(activeWords.value!!, it)
        }
        // repo.activeWords.observeForever { activeWords.value = it }
        room.activeWords.observeForever { activeWords.value = it }
        // repo.historyWords.observeForever { historyWords.value = it }
        room.historyWords.observeForever { historyWords.value = it }

        // repo.stats_data.observeForever { stats_data.value = it }
        room.currentStats.observeForever { statsData.value = it }
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
            // repo.fetchWords(s)
            room.getWordsLike(s)
        }
    }

    fun setNewWord(newWord: WordData) {
        viewModelScope.launch(Dispatchers.IO) {
            // repo.setNewWord(newWord)
            room.addNewWord(newWord)
        }
    }

    fun translateWord(word: String, lang: String) {
        viewModelScope.launch(Dispatchers.IO) {
            translator.translateWord(word, lang)
        }
    }

    fun updateWord(word: WordData, guessed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            // repo.updateSearchedWord(word, guessed)
            room.updateSearchedWord(word, guessed)
        }
    }

    fun getStatsData(){
        viewModelScope.launch(Dispatchers.IO) {
            // repo.getSearchedWords()
            room.getStatsData()
        }
    }


    private fun setTranslators() {
        for(tr in LanguageManager.getLanguages()) {
            viewModelScope.launch(Dispatchers.IO) {
                translator.setNewTranslator(Locale.getDefault().language, tr)
            }
        }
    }

    private fun fetchHistoryWords() {
        viewModelScope.launch(Dispatchers.IO) {
            // repo.fetchHistoryWords()
            room.getHistoryWords()
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