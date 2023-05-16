package org.progmob.langsupport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import org.progmob.langsupport.databinding.ActivityMainBinding
import org.progmob.langsupport.model.DataViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.setOnClickListener {
            viewModel.signUpUser(
                binding.email.text.toString(),
                binding.password.text.toString())
        }

        binding.signIn.setOnClickListener {
            viewModel.signInUser(
                binding.email.text.toString(),
                binding.password.text.toString())
        }

        binding.signOut.setOnClickListener { viewModel.signOutUser() }

        binding.fetch.setOnClickListener { viewModel.fetchLanguages() }

        binding.fetch.setOnLongClickListener {
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)
            true
        }

        viewModel.currUser.observe(this) {
            if(it != null)
                binding.text.text = "Signed in as ${it.email}"
            else
                binding.text.text = "Signed out from user"
        }

        viewModel.languages.observe(this) {
            var s = ""
            for (document in it) {
                s += "${document.value} => ${document.key}\n"
            }
            binding.text.text = s
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}