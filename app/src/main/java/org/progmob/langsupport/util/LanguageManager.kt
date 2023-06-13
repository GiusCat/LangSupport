package org.progmob.langsupport.util

import org.progmob.langsupport.R

object LanguageManager {
    private val langFlagMap: Map<String, Int> = mapOf(
        "it" to R.mipmap.ic_italian_flag_round,
        "en" to R.mipmap.ic_english_flag,
        "de" to R.mipmap.ic_german_flag_round,
        "fr" to R.mipmap.ic_french_flag,
        "es" to R.mipmap.ic_spanish_flag
    )

    val hashmap:Map<String, String> = mapOf(

        "German" to "de",
        "English" to "en",
        "Spanish" to "es",
        "French" to "fr",
        "Italian" to "it"
    )

    fun flagOf(lang: String): Int {
        return langFlagMap[lang] ?: R.mipmap.ic_launcher_round
    }

    fun getLanguages(): List<String> = langFlagMap.keys.toList()

}