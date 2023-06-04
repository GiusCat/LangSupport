package org.progmob.langsupport.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import org.progmob.langsupport.model.database.WordDatabase
import java.util.Date

object RoomRepository {
    private lateinit var db: WordDatabase
    val activeWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val historyWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val lastAddedWord: MutableLiveData<WordData> = MutableLiveData()
    val currentStats: MutableLiveData<StatsData> = MutableLiveData()
    val prefsWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val activeWordsPrefs:MutableLiveData<List<WordData>> = MutableLiveData(listOf())

    fun initDatabase(context: Context) {
        db = Room.databaseBuilder(context, WordDatabase::class.java, "words").build()
    }

    suspend fun addNewWord(word: WordData) {
        db.wordDao().insertWord(word)
        lastAddedWord.postValue(word)
        updateHistoryWords(word)
    }

    suspend fun getWordsLike(s: CharSequence?) {
        activeWords.postValue(
            if(s.isNullOrEmpty()) listOf() else db.wordDao().getWordsLike(s.toString()))
    }

    suspend fun getWordsLikePrefs(s:CharSequence){
        activeWordsPrefs.postValue(
            if(s.isEmpty()) listOf() else db.wordDao().getWordsLikePrefs(s.toString(), "true")
        )
    }

    suspend fun updateSearchedWord(word: WordData, isGuessed: Boolean) {
        word.apply {
            searched++
            guessed += if(isGuessed) 1 else 0
            timestamp = Date()
        }
        db.wordDao().updateWord(word)
        updateHistoryWords(word)
    }

    suspend fun updateFav(word: String, b:Boolean){
        if(b == false)
            db.wordDao().updateFav(word, "true")
        else
            db.wordDao().updateFav(word, "false")
    }

    suspend fun getHistoryWords() {
        historyWords.postValue(db.wordDao().getWordsOrdered().take(3))
    }

    suspend fun prefsHistoryWords(){
        prefsWords.postValue(db.wordDao().updatePrefs("true"))
    }

    suspend fun getStatsData() {
        currentStats.postValue(db.wordDao().getStatsData())
    }

    private fun updateHistoryWords(word: WordData) {
        val newL = mutableListOf(word).apply { addAll(historyWords.value!!.take(2).filter { it != word }) }
        historyWords.postValue(newL.toList())
    }
}