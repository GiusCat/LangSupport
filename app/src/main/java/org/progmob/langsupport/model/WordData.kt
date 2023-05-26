package org.progmob.langsupport.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class WordData(
    val word: String = "",
    val translation: List<String> = listOf(),
    val lang: DocumentReference? = null, // TODO: convert 'lang' to String
    val info: String? = null,
    var searched: Int = 1,
    var guessed: Int = 1,
    @ServerTimestamp
    val timestamp: Date = Date()
) {

    // Needed for query optimisation, can't be private because of serialization
    val wordIndex: String = word.lowercase()

    override fun equals(other: Any?): Boolean {
        return other != null && other::class == WordData::class && word == (other as WordData).word
    }

    override fun toString(): String {
        return "[$searched, $guessed] $word{$wordIndex} -> $translation (${lang?.path}); info=\"$info\" (created $timestamp)"
    }

    override fun hashCode(): Int {
        var result = word.hashCode()
        result = 31 * result + translation.hashCode()
        result = 31 * result + (lang?.hashCode() ?: 0)
        result = 31 * result + (info?.hashCode() ?: 0)
        result = 31 * result + searched
        result = 31 * result + guessed
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + wordIndex.hashCode()
        return result
    }
}