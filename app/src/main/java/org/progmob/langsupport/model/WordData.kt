package org.progmob.langsupport.model

import com.google.firebase.firestore.DocumentReference

class WordData(
    val word: String,
    val translation: String,
    val lang: DocumentReference,
    val info: String,
    val searched: Int = 1,
    val guessed: Int = 1) {

    override fun toString(): String {
        return "[$searched, $guessed] $word -> $translation (${lang.path}); info=$info "
    }
}