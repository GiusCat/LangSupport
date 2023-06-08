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
    val activeFavWords: MutableLiveData<List<WordData>> = MutableLiveData(listOf())
    val lastAddedWord: MutableLiveData<WordData> = MutableLiveData()
    val currentStats: MutableLiveData<StatsData> = MutableLiveData()

    fun initDatabase(context: Context) {
        db = Room.databaseBuilder(context, WordDatabase::class.java, "words").build()
    }

    suspend fun addNewWord(word: WordData) {
        db.wordDao().insertWord(word)
        lastAddedWord.postValue(word)
        updateHistoryWords(word)
    }

    suspend fun getHistoryWords() {
        historyWords.postValue(db.wordDao().getWordsOrdered().take(3))
    }

    suspend fun getWordsLike(s: CharSequence?) {
        activeWords.postValue(
            if(s.isNullOrEmpty()) listOf() else db.wordDao().getWordsLike(s.toString()))
    }

    suspend fun getFavWordsLike(s: String) {
        activeFavWords.postValue(db.wordDao().getFavWordsLike(s).sortedBy { it.word.lowercase() })
    }

    suspend fun addWordMeaning(wordData: WordData, newMeaning: String) {
        val newL = wordData.translation.toMutableList().apply { add(newMeaning) }
        wordData.translation = newL.toList()
        db.wordDao().updateWord(wordData)
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

    /*
    * Changes the "favourite" status of a word and updates the database
    */
    suspend fun updateFavouriteWord(word: WordData) {
        word.favourite = !word.favourite
        db.wordDao().updateWord(word)

        var copy = activeFavWords.value!!.map { it }.toMutableList()
        if(word.favourite) {
            copy.add(word)
        } else {
            copy = copy.filter { !word.word.equals(it.word, ignoreCase = true) }.toMutableList()
        }
        activeFavWords.postValue(copy.sortedBy { it.word.lowercase() })
    }


    suspend fun getStatsData() {
        currentStats.postValue(db.wordDao().getStatsData())
    }

    private fun updateHistoryWords(word: WordData) {
        val newL = mutableListOf(word).apply { addAll(historyWords.value!!.take(2).filter { it != word }) }
        historyWords.postValue(newL.toList())
    }
}