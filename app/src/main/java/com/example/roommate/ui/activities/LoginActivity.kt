package com.example.roommate.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.roommate.databinding.ActivityLoginBinding

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
            startActivity((Intent(this, HomeActivity::class.java)))
            finish()
        })
    }
}