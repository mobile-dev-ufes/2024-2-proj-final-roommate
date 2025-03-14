package com.example.roommate.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.ActivityLoginBinding
import com.example.roommate.utils.userManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpTv.setOnClickListener({
            if(checkFields() && authenticate()){
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
        })

        binding.signInBtn.setOnClickListener({
            startActivity((Intent(this, HomeActivity::class.java)))
            finish()
        })
    }

    private fun authenticate(): Boolean{
        userManager.user = UserModel(email = binding.emailEt.text.toString())

        return true
    }

    private fun checkFields(): Boolean{
        if(binding.emailEt.text.isEmpty() || binding.emailEt.text.isEmpty()){
            Toast.makeText(
                this,
                "Preencha todos os campos",
                Toast.LENGTH_SHORT
            ).show()

            return false
        }
        return true
    }
}