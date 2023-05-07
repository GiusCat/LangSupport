package org.progmob.langsupport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.progmob.langsupport.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
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
    }
}