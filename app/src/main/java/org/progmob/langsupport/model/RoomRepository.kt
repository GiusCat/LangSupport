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

    suspend fun addNewWord(wordData: WordData) {
        val old = getWord(wordData.word)
        if(old == null)
            db.wordDao().insertWord(wordData)
        else
            db.wordDao().updateWord(old.apply { deleted = false; favourite = false })
        lastAddedWord.postValue(wordData)
        updateHistoryWords(wordData)
    }

    suspend fun getWord(s: String): WordData? {
        return db.wordDao().getWord(s)
    }

    suspend fun getHistoryWords() {
        historyWords.postValue(
            db.wordDao().getWordsOrdered().filter { !it.deleted }.take(3))
    }

    suspend fun getWordsLike(s: CharSequence?) {
        activeWords.postValue(
            if(s.isNullOrEmpty())
                listOf()
            else
                db.wordDao().getWordsLike(s.toString()).filter { !it.deleted })
    }

    suspend fun getFavWordsLike(s: String) {
        activeFavWords.postValue(db.wordDao().getFavWordsLike(s)
            .filter { !it.deleted }
            .sortedBy { it.word.lowercase() })
    }

    suspend fun addWordMeaning(wordData: WordData, newMeaning: String) {
        val newL = wordData.translation.toMutableList().apply { add(newMeaning) }
        wordData.translation = newL.toList()
        wordData.timestamp = Date()
        db.wordDao().updateWord(wordData)
    }

    suspend fun updateWord(word: WordData) {
        db.wordDao().updateWord(word)
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
        word.timestamp = Date()
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

    suspend fun deleteWord(word: WordData) {
        word.deleted = true
        db.wordDao().updateWord(word)
        getHistoryWords()

        val newActiveWords = activeWords.value?.toMutableList()
        if(newActiveWords!!.size == 1 && newActiveWords[0].word == word.word)
            activeWords.postValue(listOf())
        else
            activeWords.postValue(newActiveWords.filter{ !it.deleted }.toList())
    }

    private fun updateHistoryWords(word: WordData) {
        val newL = mutableListOf(word).apply { addAll(historyWords.value!!.take(2).filter { it != word }) }
        historyWords.postValue(newL.toList())
    }
}