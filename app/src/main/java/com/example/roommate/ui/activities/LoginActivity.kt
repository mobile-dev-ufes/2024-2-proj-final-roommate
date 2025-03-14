package com.example.roommate.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.ActivityLoginBinding
import com.example.roommate.utils.userManager
import com.example.roommate.viewModel.UserViewModel
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
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        })
        binding.signInBtn.setOnClickListener({
            userManager.user = UserModel(email= "daniel@gmail.com")
            startActivity((Intent(this, HomeActivity::class.java)))
            finish()
        })
    }
}