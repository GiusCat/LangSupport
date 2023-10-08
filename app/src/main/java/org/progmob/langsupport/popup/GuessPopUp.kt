package org.progmob.langsupport.popup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import org.progmob.langsupport.databinding.PopUpGuessBinding
import org.progmob.langsupport.model.WordData

/*
* Pop-up dialog that shows when the user clicks on an entry that was previously added.
* The dialog lets the user guess the word and shows the result of their guess
*/
class GuessPopUp(private val wordData: WordData) : DialogFragment() {
    private lateinit var binding: PopUpGuessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopUpGuessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tryButton.setOnClickListener {
            val tryText = binding.tryTranslate.text.toString().lowercase().trim()
            val showPop = ResultPopUp(wordData, wordData.translation.indexOf(tryText))
            showPop.show((activity as AppCompatActivity).supportFragmentManager, "showRight")
            this.dismiss()
        }

        binding.tryTranslate.requestFocus()
        binding.tryTranslate.post {
            (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(binding.tryTranslate, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}