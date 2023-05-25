package org.progmob.langsupport.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.util.Date

data class WordData(
    val word: String = "",
    val translation: List<String> = listOf(),
    val lang: DocumentReference? = null, // TODO: convert 'lang' to String
    val info: String? = null,
    val searched: Int = 1,
    val guessed: Int = 1,
    val timestamp: Timestamp = Timestamp(Date())
) {

    // Needed for query optimisation, can't be private because of serialization
    val wordIndex: String = word.lowercase()

    override fun toString(): String {
        return "[$searched, $guessed] $word{$wordIndex} -> $translation (${lang?.path}); info=\"$info\" (created $timestamp)"
    }
}