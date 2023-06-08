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
import org.progmob.langsupport.databinding.PopUpAddWordBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData

class AddWordPopUp(private val wordToAdd: String) : DialogFragment() {
    private lateinit var binding: PopUpAddWordBinding
    private val viewModel: DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopUpAddWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addWordEdit.setText(wordToAdd)

        binding.sendButton.setOnClickListener {
            val word = addWord(
                binding.addWordEdit.text.toString().trim(),
                binding.addTranslationEdit.text.toString().lowercase().trim(),
                binding.addInfoEdit.text.toString()
            )
            binding.addTextFragment.text = word.toString()
            Toast.makeText(context, "$wordToAdd added!", Toast.LENGTH_SHORT).show()
            this.dismiss()
        }

        binding.addWordEdit.addTextChangedListener { text ->
            binding.sendButton.isEnabled = (text.toString() != "")
        }

        binding.translateButton.setOnClickListener {
            viewModel.translateWord(wordToAdd, "de")

            // Observer is put here to get just the last translated word
            viewModel.translatedWord.observe(viewLifecycleOwner) {
                if(it.isNullOrEmpty())
                    Toast.makeText(context, "Translator is not available right now!", Toast.LENGTH_LONG).show()
                else
                    binding.addTranslationEdit.setText(it)
            }
        }

        binding.addExitButton.setOnClickListener {
            this.dismiss()
        }
    }

    private fun addWord(word: String, trans: String, info: String): WordData? {
        if(!viewModel.isUserSignedIn()) {
            // TODO: notification of some kind
            Log.w(TAG, "User not logged in!")
            return null
        }

        // TODO: dynamic language selection
        val newWord = WordData(word, listOf(trans), "de", info)

        viewModel.addNewWord(newWord)
        return newWord
    }

    companion object {
        private const val TAG = "DataActivity"
    }
}