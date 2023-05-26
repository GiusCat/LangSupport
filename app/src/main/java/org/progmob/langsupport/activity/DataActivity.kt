package org.progmob.langsupport.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.DocumentReference
import org.progmob.langsupport.databinding.ActivityDataBinding
import org.progmob.langsupport.model.DataViewModel
import org.progmob.langsupport.model.WordData

class DataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataBinding
    private val viewModel: DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataBinding.inflate(layoutInflater)
        binding.send.isEnabled = false
        setContentView(binding.root)

        binding.send.setOnClickListener {
            val word = addWord(
                binding.word.text.toString(),
                binding.translation.text.toString().lowercase(),
                binding.info.text.toString()
            )
            binding.text.text = word.toString()
        }

        binding.word.addTextChangedListener { text ->
            Log.i(TAG, "${ text.toString() != "" }")
            binding.send.isEnabled = (text.toString() != "")
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