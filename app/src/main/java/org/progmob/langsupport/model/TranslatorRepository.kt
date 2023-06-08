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

        translatorResult.postValue(translator?.translate(word)?.await())
    }

    fun closeTranslators() {
        for(tr in activeTranslators) {
            tr.value.close()
        }
    }

//    fun foo() {
//        val model = TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.GERMAN).setTargetLanguage(
//            TranslateLanguage.ITALIAN).build()
//
//        val itaGerTrans: Translator = Translation.getClient(model)
//
//        val condition = DownloadConditions.Builder().requireWifi().build()
//        itaGerTrans.downloadModelIfNeeded(condition).addOnSuccessListener { Log.i(SearchFragment.MSG, "download completed") }
//
//        itaGerTrans.translate("Word goes here").addOnSuccessListener {
//
//                translatedText -> modifyListWords(translatedText)
//
//        }.addOnFailureListener { exception -> Log.i(SearchFragment.MSG, "not found") }
//    }

//    fun modifyListWords(translatedWord: String){
//
//        val newWords = mutableListOf<WordData>()
//        val wordToTraslate = "Word goes here"
//
//        //prendiamo le prime due righe della vecchia lista
//        // per metterle al secondo e terzo posto
//
//        val oldList: ListView? = getView()?.findViewById(R.id.listViewLastWords)
//        val oldFirst: View? = oldList?.getChildAt(0)
//        val oldFirstWord: TextView? = oldFirst?.findViewById(R.id.foreign_word)
//        val oldSecondWord: TextView? = oldFirst?.findViewById(R.id.translated_word)
//
//        val oldSecond: View? = oldList?.getChildAt(1)
//        val oldFirstWord2: TextView? = oldSecond?.findViewById(R.id.foreign_word)
//        val oldSecondWord2: TextView? = oldSecond?.findViewById(R.id.translated_word)
//
//
//        //aggiungiamo la nuova parola cercata al primo posto e le due vecchie di seguito
//        if(wordToTraslate?.text.toString() == translatedWord)
//            newWords.add(WordData(wordTr.toString(), listOf("not found")))
//        else
//            newWords.add(WordData(wordTr.toString(), listOf(translatedWord)))
//
//        newWords.add(WordData(oldFirstWord?.text.toString(), listOf(oldSecondWord?.text.toString())))
//        newWords.add(WordData(oldFirstWord2?.text.toString(), listOf(oldSecondWord2?.text.toString())))
//
//
//        //chiamate di default per adapter
//
//        val adapter = MainActivityAdapter(requireContext(), R.layout.activity_list_item, newWords)
//        Log.i(SearchFragment.MSG, translatedWord)
//        val listView : ListView? = getView()?.findViewById(R.id.listViewLastWords)
//        listView?.adapter = adapter
//        adapter.notifyDataSetChanged()
//        Log.i(SearchFragment.MSG, translatedWord)
//
//        //svuotiamo la casella di inserimento dopo l'inserimento in lista
//        wordToTraslate?.setText("")
//    }
}