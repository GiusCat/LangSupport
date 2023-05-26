package org.progmob.langsupport.util

import org.progmob.langsupport.R

object LanguageManager {
    private val langFlagMap: Map<String, Int> = mapOf(
        "it" to R.mipmap.ic_italian_flag_round,
        "en" to R.mipmap.ic_italian_flag_round,
        "de" to R.mipmap.ic_german_flag_round
    )

    fun flagOf(lang: String): Int {
        return langFlagMap[lang] ?: R.mipmap.ic_launcher_round
    }

}