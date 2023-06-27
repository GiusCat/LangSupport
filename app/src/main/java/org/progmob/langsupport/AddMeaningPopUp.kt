package org.progmob.langsupport

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import org.progmob.langsupport.databinding.PopUpAddMeaningBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData

class AddMeaningPopUp(private val wordData: WordData) : DialogFragment() {
    private lateinit var binding: PopUpAddMeaningBinding
    private val viewModel: DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopUpAddMeaningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.wordToAddMean.text = wordData.word

        binding.newMeaning.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.addMeaningButton.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.addMeaningButton.setOnClickListener {
            viewModel.addWordMeaning(wordData, binding.newMeaning.text.toString().lowercase().trim())
            Toast.makeText(context, "New meaning added successfully!", Toast.LENGTH_LONG).show()
            this.dismiss()
        }

        binding.exitMeaningButton.setOnClickListener {
            this.dismiss()
        }
    }
}