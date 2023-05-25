package org.progmob.langsupport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import org.progmob.langsupport.databinding.GuessPopUpBinding
import org.progmob.langsupport.model.WordData

/*
* Pop-up dialog that shows when the user clicks on an entry that was previously added.
* The dialog lets the user guess the word and shows the result of their guess
*/
class GuessPopUp(private val wordData: WordData) : DialogFragment() {
    private lateinit var binding: GuessPopUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GuessPopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tryButton.setOnClickListener {
            val tryText = binding.tryTranslate.text.toString().lowercase().trim()

            if(wordData.translation.contains(tryText)) {
                this.dismiss()
                val showPop = Right_PopUp(tryText, wordData.word, true)
                showPop.show((activity as AppCompatActivity).supportFragmentManager, "showRight")
            }
            else {
                val showPop = Right_PopUp(tryText, wordData.word, false)
                showPop.show((activity as AppCompatActivity).supportFragmentManager, "showRight")
            }
        }
    }

}