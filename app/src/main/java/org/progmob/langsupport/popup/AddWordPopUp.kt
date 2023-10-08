package org.progmob.langsupport.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import org.progmob.langsupport.adapter.SpinnerAdapter
import org.progmob.langsupport.databinding.PopUpAddWordBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.util.LanguageManager

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

        viewModel.errorMsg.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.confirmButton.setOnClickListener {
            viewModel.addNewWord(WordData(
                binding.addWordEdit.text.toString().trim(),
                listOf(binding.addTranslationEdit.text.toString().lowercase().trim()),
                binding.languageSpinner.selectedItem.toString(),
                binding.addInfoEdit.text.toString()
            ))
            Toast.makeText(context, "${binding.addWordEdit.text} added!", Toast.LENGTH_SHORT).show()
            this.dismiss()
        }

        binding.addWordEdit.addTextChangedListener { text ->
            binding.confirmButton.isEnabled =
                !(text.isNullOrEmpty() || binding.addTranslationEdit.text.isNullOrEmpty())
        }

        binding.addTranslationEdit.addTextChangedListener { text ->
            binding.confirmButton.isEnabled =
                !(text.isNullOrEmpty() || binding.addWordEdit.text.isNullOrEmpty())
        }

        binding.translateButton.setOnClickListener {
            viewModel.translateWord(
                binding.addWordEdit.text.toString(),
                binding.languageSpinner.selectedItem.toString())

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

        binding.languageSpinner.apply {
            adapter = SpinnerAdapter(requireContext())
            setSelection(LanguageManager.getLanguages().indexOf(viewModel.lastLang))
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {}

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
        }
    }
}