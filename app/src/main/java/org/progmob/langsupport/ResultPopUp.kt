package org.progmob.langsupport

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import org.progmob.langsupport.databinding.PopUpResultBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData

class ResultPopUp(
    private val wordData: WordData,
    private val guessedIndex: Int
) : DialogFragment() {
    private lateinit var binding: PopUpResultBinding
    private val viewModel: DataViewModel by activityViewModels()
    private val guessed = guessedIndex >= 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopUpResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateSearchedWord(wordData, guessed)

        if(guessed) {
            binding.textGuessedFragment.text = "Great! You have guessed"
            binding.section1frag.setBackgroundColor(resources.getColor(R.color.lightGreen))
        } else {
            binding.textGuessedFragment.text = "What a pity!"
            binding.section1frag.setBackgroundColor(resources.getColor(R.color.red))
        }
        binding.tedWordGuessed.text = wordData.word
        binding.itaWordGuessed.text =
            wordData.translation.getOrElse(guessedIndex){ wordData.translation[0] }

        Log.i("info", wordData.word)
        viewModel.getInfo(wordData.word)
        viewModel.infoWord.observe(viewLifecycleOwner){
            binding.infoText.text = it
        }

        binding.exit.setOnClickListener {
            this.dismiss()
        }

        binding.addMean.setOnClickListener {
            val showPop = AddMeaningPopUp(wordData)
            showPop.show((activity as AppCompatActivity).supportFragmentManager, "addMean")
        }
    }
}