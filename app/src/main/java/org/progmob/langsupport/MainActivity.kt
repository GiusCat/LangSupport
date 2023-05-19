package org.progmob.langsupport

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions


class MainActivity : Fragment(){

    val MSG: String? = this::class.simpleName
    val MSG2: String? = this::class.simpleName
    val MSG4: String? = this::class.simpleName

    var translator:Translator? = null
    var wordTr:String? = null

    var mycontext:Context? = null

    var guessed:String? = null
    companion object{
        //extra message in questo modo è dichiarato come statica
        val Extra_MSG:String = "org.progmob.langsupport"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mycontext = container?.context

        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.translateButton)?.setOnClickListener {
            Verify_Translate_Word()
        }

        view.findViewById<View>(R.id.tryButton)?.setOnClickListener {
            Verify_Try()
        }
         val model = TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.GERMAN).setTargetLanguage(TranslateLanguage.ITALIAN).build()

         val itaGerTrans: Translator = Translation.getClient(model)

         val condition = DownloadConditions.Builder().requireWifi().build()
         itaGerTrans.downloadModelIfNeeded(condition).addOnSuccessListener { Log.i(MSG, "download completed") }

         this.translator = itaGerTrans

         val words_list = initWordsList()

         if(words_list.size > 0){

             val adapter = MainActivityAdapter(mycontext!!, R.layout.activity_list_item, words_list)

             val listView : ListView? = getView()?.findViewById(R.id.listViewLastWords)
             listView?.adapter = adapter
             adapter.notifyDataSetChanged()
         }
    }

    // inizializzazione della lista tedesco-italiano

    private fun initWordsList():MutableList<ActivityData>{

        val activityList = mutableListOf<ActivityData>()
        activityList.add(ActivityData("apfel", "mela"))
        activityList.add(ActivityData("hallo", "ciao"))
        activityList.add(ActivityData("danke", "grazie"))

        return activityList
    }

    private fun Verify_Translate_Word() {

        val wordToTraslate = getView()?.findViewById<EditText>(R.id.InsertWordTranslate)
        this.wordTr = wordToTraslate?.text.toString()

        // aggiungere ricerca in DB e settare correttamente variabile sotto
        val already_searched: Boolean = true

        if(already_searched){

            val showPop = PopUp(wordTr!!, translator)
            showPop.show((activity as AppCompatActivity).supportFragmentManager, "showPop")

        }
        else {

            this.translator?.translate(wordToTraslate?.text.toString())?.addOnSuccessListener {

                    translatedText ->
                modifyListWords(translatedText)

            }?.addOnFailureListener { exception -> Log.i(MSG2, "not found") }

        }

    }

    fun Verify_Try() {

        translator?.translate(wordTr.toString())?.addOnSuccessListener {
            translatedText -> ConfirmRightWrong(translatedText)
        }?.addOnFailureListener { exception -> Log.i(MSG2, "not found") }
    }

    private fun modifyListWords(translatedWord: String){

        val newWords = mutableListOf<ActivityData>()
        val wordToTraslate = getView()?.findViewById<EditText>(R.id.InsertWordTranslate)

        //prendiamo le prime due righe della vecchia lista
        // per metterle al secondo e terzo posto

        val oldList: ListView? = getView()?.findViewById(R.id.listViewLastWords)
        val oldFirst: View? = oldList?.getChildAt(0)
        val oldFirstWord:TextView? = oldFirst?.findViewById(R.id.italianWord)
        val oldSecondWord:TextView? = oldFirst?.findViewById(R.id.translatedWord)

        val oldSecond: View? = oldList?.getChildAt(1)
        val oldFirstWord2:TextView? = oldSecond?.findViewById(R.id.italianWord)
        val oldSecondWord2:TextView? = oldSecond?.findViewById(R.id.translatedWord)

        //aggiungiamo la nuova parola cercata al primo posto e le due vecchie di seguito
        if(wordToTraslate?.text.toString() == translatedWord)
            newWords.add(ActivityData(wordToTraslate?.text.toString(), "not found"))
        else
            newWords.add(ActivityData(wordToTraslate?.text.toString(), translatedWord))

        newWords.add(ActivityData(oldFirstWord?.text.toString(), oldSecondWord?.text.toString()))
        newWords.add(ActivityData(oldFirstWord2?.text.toString(), oldSecondWord2?.text.toString()))

        //chiamate di default per adapter

        val adapter = MainActivityAdapter(mycontext!!, R.layout.activity_list_item, newWords)
        val listView : ListView? = getView()?.findViewById(R.id.listViewLastWords)
        listView?.adapter = adapter
        adapter.notifyDataSetChanged()

        //svuotiamo la casella di inserimento dopo l'inserimento in lista
        wordToTraslate?.setText("")
    }

    private fun ConfirmRightWrong(translatedText: String?) {

        val translated:String = getActivity()?.findViewById<EditText>(R.id.tryTranslate)?.text.toString()

        if(translated == translatedText) this.guessed == "guessed"
        else this.guessed == "unguessed"

        Log.i(MSG, guessed.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        this.translator?.close()
    }


}