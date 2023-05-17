package org.progmob.langsupport.model

import com.google.firebase.firestore.DocumentReference

data class WordData(
    val word: String = "",
    val translation: List<String> = listOf(),
    val lang: DocumentReference? = null,
    val info: String? = null,
    val searched: Int = 1,
    val guessed: Int = 1) {

    // Needed for query optimisation, can't be private because of serialization
    val wordIndex: String = word.lowercase()

    override fun toString(): String {
        return "[$searched, $guessed] $word{$wordIndex} -> $translation (${lang?.path}); info=\"$info\""
    }
}