package org.progmob.langsupport.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.model.StatsData

@Dao
interface WordDao {
    @Insert
    suspend fun insertWord(word: WordData)

    @Insert
    suspend fun insertWordList(wordList: List<WordData>)

    @Update
    suspend fun updateWord(word: WordData)

    @Delete
    suspend fun deleteWord(word: WordData)

    @Query("SELECT * FROM WordData WHERE word = :s")
    suspend fun getWord(s: String): WordData?

    @Query("SELECT * FROM WordData")
    suspend fun getAllWords(): List<WordData>

    @Query("SELECT * FROM WordData WHERE word LIKE :s || '%'")
    suspend fun getWordsLike(s: String): List<WordData>

    @Query("SELECT * FROM WordData WHERE favourite = 1 AND word LIKE :s || '%' ORDER BY word DESC")
    suspend fun getFavWordsLike(s: String): List<WordData>

    @Query("SELECT sum(searched) AS searched, sum(guessed) AS guessed FROM WordData")
    suspend fun getStatsData(): StatsData?

    @Query("SELECT * FROM WordData ORDER BY timestamp DESC")
    suspend fun getWordsOrdered(): List<WordData>

    @Query("SELECT info FROM WordData WHERE word = :w")
    suspend fun getInfo(w:String):String
}