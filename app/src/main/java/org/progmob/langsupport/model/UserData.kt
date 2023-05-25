package org.progmob.langsupport.model

import com.google.firebase.firestore.DocumentReference

data class UserData(
    val name: String = "",
    val mainLang: DocumentReference? = null,
    // val knownLang: List<DocumentReference> = listOf()
) {
    override fun toString(): String {
        return "$name: ${mainLang?.id}"
    }
}