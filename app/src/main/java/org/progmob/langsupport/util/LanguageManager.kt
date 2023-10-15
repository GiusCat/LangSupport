package org.progmob.langsupport.util

import org.progmob.langsupport.R

object LanguageManager {
    private val languages = listOf("it", "en", "de", "fr", "es")

    private val langFlagMap: Map<String, Int> = mapOf(
        "it" to R.drawable.italian_flag,
        "en" to R.drawable.english_flag,
        "de" to R.drawable.german_flag,
        "fr" to R.drawable.french_flag,
        "es" to R.drawable.spanish_flag
    )

    private val langNameMap: Map<String, String> = mapOf(
        "it" to "ITA",
        "en" to "ENG",
        "de" to "GER",
        "fr" to "FRA",
        "es" to "SPA"
    )

    fun flagOf(langTag: String?): Int {
        return langFlagMap[langTag] ?: R.mipmap.ic_launcher_round
    }

    fun flagOf(langIndex: Int): Int {
        if(!languages.indices.contains(langIndex)) return R.mipmap.ic_launcher_round
        return langFlagMap[languages[langIndex]] ?: R.mipmap.ic_launcher_round
    }

    fun nameOf(langTag: String): String {
        return langNameMap[langTag] ?: "---"
    }

    fun nameOf(langIndex: Int): String {
        if(!languages.indices.contains(langIndex)) return "---"
        return langNameMap[languages[langIndex]] ?: "---"
    }

    fun getLanguages(): List<String> = languages
}