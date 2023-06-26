package org.progmob.langsupport.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.progmob.langsupport.databinding.ActivityLoginBinding
import org.progmob.langsupport.model.DataViewModel


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.currUser.observe(this) {
            if(it != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        viewModel.errorMsg.observe(this) {
            if(!it.isNullOrEmpty())
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }

        binding.signUp.setOnClickListener {
            viewModel.signUpUser(
                binding.email.text.toString(), binding.password.text.toString())
        }

        binding.signIn.setOnClickListener {
            viewModel.signInUser(
                binding.email.text.toString(), binding.password.text.toString())
        }
    }
}