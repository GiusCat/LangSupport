package org.progmob.langsupport.util

import org.progmob.langsupport.R

object LanguageManager {
    private val languages = listOf("it", "en", "de", "fr", "es")

    private val langFlagMap: Map<String, Int> = mapOf(
        "it" to R.mipmap.ic_italian_flag_round,
        "en" to R.mipmap.ic_english_flag_round,
        "de" to R.mipmap.ic_german_flag_round,
        "fr" to R.mipmap.ic_french_flag_round,
        "es" to R.mipmap.ic_spanish_flag_round
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