package org.progmob.langsupport.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class WordData(
    @PrimaryKey val word: String = "",
    var translation: List<String> = listOf(),
    val lang: String = "en",
    val info: String? = null,
    var searched: Int = 1,
    var guessed: Int = 1,
    var favourite: Boolean = false,
    var deleted: Boolean = false,
    var timestamp: Date = Date()
) {
    override fun equals(other: Any?): Boolean {
        return other != null && other::class == WordData::class &&
                word.equals((other as WordData).word, ignoreCase = true) &&
                translation.size == other.translation.size
    }

    override fun toString(): String {
        return "[$searched, $guessed] $word -> $translation ($lang); info=\"$info\""
    }

    override fun hashCode(): Int {
        var result = word.hashCode()
        result = 31 * result + translation.hashCode()
        result = 31 * result + (lang.hashCode())
        result = 31 * result + (info?.hashCode() ?: 0)
        result = 31 * result + searched
        result = 31 * result + guessed
        result = 31 * result + timestamp.hashCode()
        return result
    }
}