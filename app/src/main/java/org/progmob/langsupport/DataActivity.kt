package org.progmob.langsupport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.progmob.langsupport.databinding.ActivityDataBinding
import org.progmob.langsupport.databinding.ActivityMainBinding
import org.progmob.langsupport.model.WordData

class DataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataBinding.inflate(layoutInflater)
        auth = Firebase.auth
        db = Firebase.firestore
        binding.send.isEnabled = false
        setContentView(binding.root)

        binding.send.setOnClickListener {
            val word = addWord()
            binding.text.text = word.toString()
        }

        binding.word.addTextChangedListener { text ->
            Log.i(TAG, "${ text.toString() != "" }")
            binding.send.isEnabled = (text.toString() != "")
        }
    }

    private fun addWord(): WordData? {
        if(auth.currentUser == null) {
            // TODO: notification of some kind
            Log.w(TAG, "User not logged in!")
            return null
        }

        val newWord = WordData(
            binding.word.text.toString(),
            binding.translation.text.toString(),
            db.collection("languages").document("de"),
            binding.info.text.toString()
        )

        db.collection("users/${auth.currentUser!!.uid}/words")
            .document(newWord.word)
            .set(newWord)

        return newWord
    }

    companion object {
        private const val TAG = "DataActivity"
    }
}