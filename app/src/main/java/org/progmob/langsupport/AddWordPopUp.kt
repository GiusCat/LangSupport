package org.progmob.langsupport

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.firestore.DocumentReference
import org.progmob.langsupport.databinding.ActivityAddWordBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData

class AddWordPopUp(wordToAdd: String) : DialogFragment() {

    private lateinit var binding:ActivityAddWordBinding
    private val viewModel: DataViewModel by activityViewModels()
    private val wordToAdd:String = wordToAdd

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityAddWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addWordEdit.setText(wordToAdd)

        binding.sendButton.setOnClickListener {

            val word = addWord(
                binding.addWordEdit.text.toString(),
                binding.addTranslationEdit.text.toString().lowercase(),
                binding.addInfoEdit.text.toString()
            )
            binding.addTextFragment.text = word.toString()
            this.dismiss()
            Toast.makeText(context, "${wordToAdd} Inserita!", Toast.LENGTH_SHORT).show()
        }
        binding.addWordEdit.addTextChangedListener { text ->
           // Log.i(DataActivity.TAG, "${ text.toString() != "" }")
            binding.sendButton.isEnabled = (text.toString() != "")
        }

        binding.addExitButton.setOnClickListener {
            this.dismiss()
        }

        binding.addTransaleButton.setOnClickListener {
            viewModel.translateWord(wordToAdd, "de")
            viewModel.translatedWord.observe(viewLifecycleOwner){
                binding.addTranslationEdit.setText(it)
            }
        }
    }

    private fun addWord(word: String, trans: String, info: String): WordData? {
        if(!viewModel.isUserSignedIn()) {
            // TODO: notification of some kind
            Log.w(TAG, "User not logged in!")
            return null
        }

        // TODO: dynamic language selection
        val newWord = WordData(word, listOf(trans), getGerman(), info)

        viewModel.setNewWord(newWord)
        return newWord
    }

    private fun getGerman(): DocumentReference? {
        return viewModel.languages.value?.first { it.id == "de" }
    }

    companion object {
        private const val TAG = "DataActivity"
    }
}