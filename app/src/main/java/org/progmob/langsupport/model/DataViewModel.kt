package org.progmob.langsupport.model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.progmob.langsupport.util.FirebaseUpdaterWorker
import org.progmob.langsupport.util.LanguageManager
import java.util.concurrent.TimeUnit
import kotlin.Exception

private val Context.dataStore by preferencesDataStore(name = "settings")
private val TRANSLATE_TO = stringPreferencesKey("translate_lang")
private val FIRST_RUN = booleanPreferencesKey("first_run")

class DataViewModel(private val application: Application): AndroidViewModel(application) {
    private val firebase = FirebaseRepository
    private val translator = TranslatorRepository
    private val room = RoomRepository
    private val firstRun: Flow<Boolean> = application.dataStore.data.map { it[FIRST_RUN] ?: true }
    private val translateLang: Flow<String> =
        application.dataStore.data.map { it[TRANSLATE_TO] ?: LanguageManager.getSystemLanguage() }

    var lastLang: String? = null
    val activeWords: MutableLiveData<List<WordData>> = MutableLiveData(mutableListOf())
    val activeFavWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val historyWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val translatedWord: MutableLiveData<String> = MutableLiveData("")
    val currUser: MutableLiveData<FirebaseUser> = MutableLiveData(firebase.getCurrentUser())
    val errorMsg: MutableLiveData<String> = MutableLiveData("")
    val statsData: MutableLiveData<StatsData> = MutableLiveData()

    init {
        firebase.initFirebase(runBlocking { firstRun.first() })
        room.initDatabase(application.applicationContext)

        firebase.currUser.observeForever { currUser.value = it.also { dataSetUp() } }

        room.lastAddedWord.observeForever {
            activeWords.value = newListFromCurrent(activeWords.value!!, it)
        }

        room.activeWords.observeForever { activeWords.value = it }
        room.activeFavWords.observeForever { activeFavWords.value = it }
        room.historyWords.observeForever { historyWords.value = it }

        room.currentStats.observeForever { statsData.value = it }
        room.lastLang.observeForever{lastLang = it}
        translator.translatorResult.observeForever { translatedWord.value = it }
    }

    fun signUpUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                application.dataStore.edit {
                    it[FIRST_RUN] = false
                    it[TRANSLATE_TO] = LanguageManager.getSystemLanguage()
                }
                firebase.signUpUser(email, password)
            } catch (e: Exception) {
                errorMsg.postValue(e.message)
            }
        }
    }

    fun signInUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                application.dataStore.edit {
                    it[FIRST_RUN] = false
                    it[TRANSLATE_TO] = LanguageManager.getSystemLanguage()
                }
                firebase.signInUser(email, password)
            } catch (e: Exception) {
                errorMsg.postValue(e.message)
            }
        }
    }

    fun signOutUser() {
        firebase.signOutUser()
        viewModelScope.launch(Dispatchers.IO) {
            room.deleteAllWords()
        }
    }

    @Deprecated("Unused method, soon to be deleted")
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

    fun updateWord(word: WordData) {
        viewModelScope.launch(Dispatchers.IO) {
            room.updateWord(word)
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

    fun getTranslateLanguage(): String = runBlocking { translateLang.first() }

    fun setTranslateLanguage(lang: String) {
        viewModelScope.launch(Dispatchers.IO) {
            application.dataStore.edit { it[TRANSLATE_TO] = lang }
            closeTranslators()
            setTranslators()
        }
    }

    fun setTranslators() {
        viewModelScope.launch(Dispatchers.IO) {
            translateLang.collect { translate ->
                for(tr in LanguageManager.getLanguages()) {
                    translator.setNewTranslator(translate, tr)
                }
            }
        }
    }

    @Deprecated("This method and its related object will soon be deleted, use its overload")
    fun translateWord(word: String, lang: String) {
        viewModelScope.launch(Dispatchers.IO) {
            translator.translateWord(word, lang, translateLang.first())
        }
    }

    fun translateWord(word: String, lang: String, onTranslate: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = translator.translateWordReturn(word, lang, translateLang.first())
            onTranslate(result.orEmpty())
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

    fun getUserEmail(): String = firebase.getCurrentUser()?.email!!

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
        if(firebase.getCurrentUser() == null) return

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