package org.progmob.langsupport.model

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.progmob.langsupport.util.FirebaseUpdaterWorker
import org.progmob.langsupport.util.LanguageManager
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.Exception

class DataViewModel(private val application: Application): AndroidViewModel(application) {
    private val firebase = FirebaseRepository
    private val translator = TranslatorRepository
    private val room = RoomRepository
    private val prefs: SharedPreferences =
        application.applicationContext.getSharedPreferences("first_run", Context.MODE_PRIVATE)
    val activeWords: MutableLiveData<List<WordData>> = MutableLiveData(mutableListOf())
    val activeFavWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val historyWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val translatedWord: MutableLiveData<String?> = MutableLiveData()
    val currUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val errorMsg: MutableLiveData<String> = MutableLiveData()
    val statsData: MutableLiveData<StatsData> = MutableLiveData()
    var lastLang: String? = null

    init {
        firebase.initFirebase(prefs.getBoolean("first_run", true))
        room.initDatabase(application.applicationContext)

        firebase.currUser.observeForever { currUser.value = it.also { dataSetUp() } }

        room.lastAddedWord.observeForever {
            activeWords.value = newListFromCurrent(activeWords.value!!, it)
        }

        room.activeWords.observeForever { activeWords.value = it }
        room.activeFavWords.observeForever { activeFavWords.value = it }
        room.historyWords.observeForever { historyWords.value = it }

        room.currentStats.observeForever { statsData.value = it }
        translator.translatorResult.observeForever { translatedWord.value = it }
        room.lastLang.observeForever{lastLang = it}
    }

    fun signUpUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                prefs.edit().putBoolean("first_run", false).apply()
                firebase.signUpUser(email, password)
            } catch (e: Exception) {
                errorMsg.postValue(e.message)
            }
        }
    }

    fun signInUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                prefs.edit().putBoolean("first_run", false).apply()
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

    fun deleteWord(word: WordData){
        viewModelScope.launch(Dispatchers.IO) {
            room.deleteWord(word)
        }
    }

    fun setTranslators() {
        for(tr in LanguageManager.getLanguages()) {
            viewModelScope.launch(Dispatchers.IO) {
                translator.setNewTranslator(Locale.getDefault().language, tr)
            }
        }
    }

    fun translateWord(word: String, lang: String) {
        viewModelScope.launch(Dispatchers.IO) {
            translator.translateWord(word, lang)
        }
    }

    fun closeTranslators() {
        translator.closeTranslators()
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

    fun setRegularUpdater() {
        Log.i("TAG", "SCHEDULE REGULAR UPDATER")
        val saveRequest =
            PeriodicWorkRequestBuilder<FirebaseUpdaterWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(application.applicationContext)
            .enqueueUniquePeriodicWork(
                saveRequest::class.simpleName!!,
                ExistingPeriodicWorkPolicy.UPDATE,
                saveRequest)
    }

    private fun dataSetUp() {
        if(!isUserSignedIn()) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val remoteWords = firebase.fetchAllUserWords()
                for(w in remoteWords) {
                    val localWord = room.getWord(w.word)
                    if(localWord == null) {
                        room.addNewWord(w)
                        continue
                    }
                    if(w.timestamp > localWord.timestamp)
                        // Favourite status is kept from local
                        room.updateWord(w.apply { favourite = localWord.favourite })
                }
                room.getHistoryWords()
                room.getFavWordsLike("")
                room.getLastLang()
            } catch (e: Exception) {
                Log.i("TAG", "Exception during DataSetUp - ${e.message}")
            }
        }
    }

    private fun <T> newListFromCurrent(currList: List<T>, newEl: T): List<T> {
        val newL: MutableList<T> = mutableListOf()
        for(el in currList) { newL.add(el) }
        return newL.apply { add(newEl) }.toList()
    }
}