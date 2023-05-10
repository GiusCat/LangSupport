package org.progmob.langsupport

import android.graphics.Color
import android.location.GnssAntennaInfo.Listener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import org.progmob.langsupport.R

class MainActivity : AppCompatActivity() {

    val MSG: String? = this::class.simpleName
    val MSG2: String? = this::class.simpleName
    val MSG3: String? = this::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createPieChart()

        val words_list = initWordsList()

        if(words_list.size > 0){

            val adapter = MainActivityAdapter(this, R.layout.activity_list_item, words_list)

            val listView : ListView = findViewById(R.id.listViewLastWords)
            listView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    // inizializzazione della lista italiano-tedesco

    private fun initWordsList():MutableList<ActivityData>{

        val activityList = mutableListOf<ActivityData>()
        activityList.add(ActivityData("mela", "apfel"))
        activityList.add(ActivityData("ciao", "hallo"))
        activityList.add(ActivityData("grazie", "danke"))

        return activityList
    }

    fun Verify_Translate_Word(view: View) {

        val wordToTraslate = findViewById<EditText>(R.id.InsertWordTranslate)

        // traduzione parola
        val model = TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ITALIAN).setTargetLanguage(TranslateLanguage.GERMAN).build()

        val itaGerTrans: Translator = Translation.getClient(model)

        val condition = DownloadConditions.Builder().requireWifi().build()
        itaGerTrans.downloadModelIfNeeded(condition).addOnSuccessListener { Log.i(MSG, "download completed") }

        itaGerTrans.translate(wordToTraslate.text.toString()).addOnSuccessListener {
             //translatedText -> modifyListWords(wordToTraslate.text.toString(), translatedText)
             translatedText -> Log.i(MSG2, translatedText)
         }.addOnFailureListener {exception -> Log.i(MSG3, exception.toString()) }

        // chiamata a modifica della lista
        //modifyListWords(wordToTraslate.text.toString(), tmp)

        itaGerTrans.close()

        //rendi vuoto la edit text per inserire nuova parola (dopo tutte le operazioni)
        wordToTraslate.setText("")
    }

    private fun modifyListWords(newItalianWord: String, translatedWord: String){

        val newWords = mutableListOf<ActivityData>()

        //prendiamo le prime due righe della vecchia lista
        // per metterle al secondo e terzo posto

        val oldList:ListView = findViewById(R.id.listViewLastWords)
        val oldFirst: View = oldList.getChildAt(0)
        val oldFirstWord:TextView = oldFirst.findViewById(R.id.italianWord)
        val oldSecondWord:TextView = oldFirst.findViewById(R.id.translatedWord)

        val oldSecond: View = oldList.getChildAt(1)
        val oldFirstWord2:TextView = oldSecond.findViewById(R.id.italianWord)
        val oldSecondWord2:TextView = oldSecond.findViewById(R.id.translatedWord)

        //aggiungiamo la nuova parola cercata al primo posto e le due vecchie di seguito

        newWords.add(ActivityData(newItalianWord, translatedWord))
        newWords.add(ActivityData(oldFirstWord.text.toString(), oldSecondWord.text.toString()))
        newWords.add(ActivityData(oldFirstWord2.text.toString(), oldSecondWord2.text.toString()))

        //chiamate di default per adapter

        val adapter = MainActivityAdapter(this, R.layout.activity_list_item, newWords)
        val listView : ListView = findViewById(R.id.listViewLastWords)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    private fun createPieChart(){

        val pie = findViewById<PieChart>(R.id.piechart)

        pie.addPieSlice(PieModel("Guessed", 10F, Color.parseColor("#ff0000")))
        pie.addPieSlice(PieModel("Wrong", 20F, Color.parseColor("#0000ff")))
    }
}