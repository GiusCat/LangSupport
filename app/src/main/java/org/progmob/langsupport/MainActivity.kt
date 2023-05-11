package org.progmob.langsupport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
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
        setContentView(binding.root)

        binding.signUp.setOnClickListener {
            signUpUser()
        }

        binding.signIn.setOnClickListener {
            auth.signInWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString())
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        binding.text.text = "Signed in as ${auth.currentUser?.email}"
                    } else {
                        binding.text.text = "Error while signing in!!!!"
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
                    Log.w(TAG, exception)
                    binding.text.text = "Errore di retrieval!"
                }
        }

        binding.fetch.setOnLongClickListener {
                // val: variabile immutabile; var: variabile mutabile

                // primo parametro: contesto (quindi questa attività)
                // secondo parametro: activity di arrivo
                val intent = Intent(this, DataActivity::class.java)
                startActivity(intent)
                true
        }

        auth.addAuthStateListener { auth ->
            binding.text.text = "Cambio di auth, utente attuale: ${auth.currentUser?.email}"
        }
    }

    private fun signUpUser() {
        auth.createUserWithEmailAndPassword(
            binding.email.text.toString(),
            binding.password.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    binding.text.text = "Signed up as ${auth.currentUser?.email}"
                    auth.currentUser?.let {
                        val mainLang = db.collection("languages").document("it")
                        db.collection("users").document(it.uid).set(
                            hashMapOf(
                                "main_lang" to mainLang,
                                "name" to "New user"
                            )
                        )
                    }
                } else {
                    binding.text.text = "Error while signing up!!!!"
                }
            }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}