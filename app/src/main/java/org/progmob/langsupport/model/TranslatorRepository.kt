package org.progmob.langsupport.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import java.util.Locale

object TranslatorRepository {
    private val activeTranslators: MutableMap<String, Translator> = mutableMapOf()
    val translatorResult: MutableLiveData<String?> = MutableLiveData()

    suspend fun setNewTranslator(mainLang: String, translateLang: String) {
        val sourceLangTag = TranslateLanguage.fromLanguageTag(translateLang) ?: return
        val targetLangTag = TranslateLanguage.fromLanguageTag(mainLang) ?: return
        if(sourceLangTag == targetLangTag) return

        val model = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLangTag)
            .setTargetLanguage(targetLangTag).build()

        val translator = Translation.getClient(model)
        val condition = DownloadConditions.Builder().requireWifi().build()

        try {
            translator.downloadModelIfNeeded(condition).await()
            activeTranslators[sourceLangTag] = translator
        } catch (e: MlKitException) {
            Log.w("TranslatorRepository", "${e.message}")
        }
    }

    suspend fun translateWord(word: String, lang: String) {
        val langTag = TranslateLanguage.fromLanguageTag(lang)
        val translator = activeTranslators[langTag]

        translatorResult.postValue(
            if(lang != Locale.getDefault().language)
                translator?.translate(word)?.await()
            else
                word
        )
    }

    fun closeTranslators() {
        for(tr in activeTranslators) {
            tr.value.close()
        }
    }
}