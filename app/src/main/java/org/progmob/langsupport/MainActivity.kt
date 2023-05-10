package org.progmob.langsupport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.progmob.langsupport.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
        db = Firebase.firestore
        binding.text.text = "Hello World!"
        setContentView(binding.root)

        binding.signUp.setOnClickListener {
            auth.createUserWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString())
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        binding.text.text = "Signed up as ${auth.currentUser?.email}"
                    } else {
                        binding.text.text = "Error while signing up!!!!"
                    }
                }
        }

        binding.signIn.setOnClickListener {
            auth.signInWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString())
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        binding.text.text = "Signed in as ${auth.currentUser?.email}"
                    } else {
                        binding.text.text = "Error while signing up!!!!"
                    }
                }
        }

        binding.signOut.setOnClickListener {
            auth.signOut()
        }

        binding.fetch.setOnClickListener {
            db.collection("languages")
                .get()
                .addOnSuccessListener { result ->
                    var s = ""
                    for (document in result) {
                        s += "${document.id} => ${document.data}\n"
                    }
                    binding.text.text = s
                }
                .addOnFailureListener { exception ->
                    Log.w("APP", exception)
                    binding.text.text = "Errore di retrieval!"
                }
        }

        auth.addAuthStateListener { auth ->
            binding.text.text = "Cambio di auth, utente attuale: ${auth.currentUser?.email}"
        }
    }

    companion object {
        private val TAG = this::class.toString()
    }
}