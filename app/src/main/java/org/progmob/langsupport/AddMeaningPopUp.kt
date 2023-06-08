package org.progmob.langsupport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.progmob.langsupport.databinding.PopUpAddMeaningBinding

class AddMeaningPopUp(private val tedText: String) : DialogFragment(){
    private lateinit var binding: PopUpAddMeaningBinding

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
        binding.wordToAddMean.text = tedText

        binding.addMeaningButton.setOnClickListener {
             // TODO: implement add meaning
        }

        binding.exitMeaningButton.setOnClickListener {
            this.dismiss()
        }
    }
}