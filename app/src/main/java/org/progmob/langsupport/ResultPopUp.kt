package org.progmob.langsupport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import org.progmob.langsupport.databinding.ResultPopUpBinding

class ResultPopUp(
    val word: String,
    val translation: String,
    val guessed: Boolean
) : DialogFragment() {
    private lateinit var binding: ResultPopUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ResultPopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(guessed) {
            binding.textGuessedFragment.text = "Eccellente! Hai indovinato"
            binding.section1frag.setBackgroundColor(resources.getColor(R.color.lightGreen))
        } else {
            binding.textGuessedFragment.text = "Peccato! Hai Sbagliato"
            binding.section1frag.setBackgroundColor(resources.getColor(R.color.red))
        }
        binding.itaWordGuessed.text = translation
        binding.tedWordGuessed.text = word

        binding.exit.setOnClickListener {
            this.dismiss()
        }

        binding.addMean.setOnClickListener {
            val showPop = AddMeaningPopUp(word)
            showPop.show((activity as AppCompatActivity).supportFragmentManager, "addMean")
        }

    }
}