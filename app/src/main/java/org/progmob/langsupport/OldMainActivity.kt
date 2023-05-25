package org.progmob.langsupport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import org.progmob.langsupport.databinding.ActivityMainOldBinding
import org.progmob.langsupport.model.DataViewModel

class OldMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainOldBinding
    private val viewModel: DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainOldBinding.inflate(layoutInflater)
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

        binding.search.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        binding.fetch.setOnLongClickListener {
            startActivity(Intent(this, DataActivity::class.java))
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
                s += "${document.id}\n"
            }
            binding.text.text = s
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}