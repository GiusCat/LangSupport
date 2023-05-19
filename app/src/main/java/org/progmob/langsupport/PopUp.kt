package org.progmob.langsupport

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.mlkit.nl.translate.Translator

class PopUp(wordTr: String, translator: Translator?) : DialogFragment() {

    val wordTrans = wordTr
    val translator = translator
    val MSG: String? = this::class.simpleName
    var translation: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.already_searched_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        translator?.translate(wordTrans)?.addOnSuccessListener { translatesText -> this.translation = translatesText }
        val tryEditText = view.findViewById<EditText>(R.id.tryTranslate)
        val tryButton = view.findViewById<Button>(R.id.tryButton)

        tryButton.setOnClickListener {
            val tryText = tryEditText.text.toString()
            tryText.lowercase()
            if(tryText == translation?.lowercase())
                Log.i(MSG, "Indovinato")
            else
                Log.i(MSG, "Sbagliato")
        }
    }

}