package org.progmob.langsupport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import org.progmob.langsupport.databinding.PopUpAddWordBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.util.LanguageManager

class AddWordPopUp(private val wordToAdd: String) : DialogFragment() {
    private lateinit var binding: PopUpAddWordBinding
    private val viewModel: DataViewModel by activityViewModels()
    private var currentLang: String? = null

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
        val lm = LanguageManager
        val hashLang = lm.hashmap

        binding.addWordEdit.setText(wordToAdd)

        viewModel.errorMsg.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.confirmButton.setOnClickListener {
            addWord(
                binding.addWordEdit.text.toString().trim(),
                binding.addTranslationEdit.text.toString().lowercase().trim(),
                binding.addInfoEdit.text.toString()
            )
            Toast.makeText(context, "${binding.addWordEdit.text} added!", Toast.LENGTH_SHORT).show()
            this.dismiss()
        }

        binding.addWordEdit.addTextChangedListener { text ->
            binding.confirmButton.isEnabled = (text.toString() != "")
        }

        binding.translateButton.setOnClickListener {
            viewModel.translateWord(binding.addWordEdit.text.toString(), getLang(hashLang).toString())

            // Observer is put here to get just the last translated word
            viewModel.translatedWord.observe(viewLifecycleOwner) {
                if(it.isNullOrEmpty())
                    Toast.makeText(context, "Translator is not available right now!", Toast.LENGTH_LONG).show()
                else
                    binding.addTranslationEdit.setText(it)
            }
        }

        binding.cancelButton.setOnClickListener {
            this.dismiss()
        }

        val spinner: Spinner = binding.languageSpinner

        ArrayAdapter.createFromResource(requireContext(), R.array.languages_array, android.R.layout.simple_spinner_item).also {
            adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        currentLang = parent?.getItemAtPosition(position) as String?
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        }
    }

    private fun addWord(word: String, trans: String, info: String) {
        val lm = LanguageManager
        val hashLang = lm.hashmap

        // TODO: dynamic language selection
        val newWord = WordData(word, listOf(trans), getLang(hashLang).toString(), info)

        viewModel.addNewWord(newWord)
    }

    private fun getLang(hashmap:Map<String, String>): String? {
        return hashmap[currentLang]
    }
}