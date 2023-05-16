package org.progmob.langsupport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.ktx.firestore
import org.progmob.langsupport.databinding.ActivityDataBinding
import org.progmob.langsupport.model.FirebaseRepository
import org.progmob.langsupport.model.WordData

class DataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataBinding
    private val repository = FirebaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataBinding.inflate(layoutInflater)
        binding.send.isEnabled = false
        setContentView(binding.root)

        binding.send.setOnClickListener {
            val word = addWord(
                binding.word.text.toString(),
                binding.translation.text.toString(),
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
        if(repository.getCurrentUser() == null) {
            // TODO: notification of some kind
            Log.w(TAG, "User not logged in!")
            return null
        }

        val lang = repository.fb.firestore.collection("languages").document("de")
        val newWord = WordData( word, trans, lang, info)

        repository.setNewWord(newWord)
        return newWord
    }

    companion object {
        private const val TAG = "DataActivity"
    }
}