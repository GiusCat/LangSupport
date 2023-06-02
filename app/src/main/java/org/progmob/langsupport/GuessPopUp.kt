package org.progmob.langsupport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import org.progmob.langsupport.databinding.GuessPopUpBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData

/*
* Pop-up dialog that shows when the user clicks on an entry that was previously added.
* The dialog lets the user guess the word and shows the result of their guess
*/
class GuessPopUp(private val wordData: WordData) : DialogFragment() {
    private lateinit var binding: GuessPopUpBinding
    private val viewModel: DataViewModel by activityViewModels()

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
            this.dismiss()

            if(wordData.translation.contains(tryText)) {
                val showPop = ResultPopUp(wordData.word, tryText, true)
                showPop.show((activity as AppCompatActivity).supportFragmentManager, "showRight")
            }
            else {
                val showPop = ResultPopUp(wordData.word, wordData.translation[0], false)
                showPop.show((activity as AppCompatActivity).supportFragmentManager, "showRight")
            }

            viewModel.updateWord(wordData, wordData.translation.contains(tryText))
        }
    }

}