package org.progmob.langsupport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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
            if(tryText == translation?.lowercase()) {
                this.dismiss()

                val showPop = Right_PopUp(translation!!, wordTrans, true)
                showPop.show((activity as AppCompatActivity).supportFragmentManager, "showRight")
            }
            else {
                val showPop = Right_PopUp(translation!!, wordTrans, false)
                showPop.show((activity as AppCompatActivity).supportFragmentManager, "showRight")
            }
        }
    }

}