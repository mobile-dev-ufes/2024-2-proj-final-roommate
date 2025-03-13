package com.example.roommate.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.roommate.databinding.ActivityLoginBinding
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
            startActivity((Intent(this, HomeActivity::class.java)))
            finish()
        })

        // Apenas para testar o Firebase
        val db = Firebase.firestore

        val jogMap = hashMapOf(
            "name" to "Letícia",
            "nick" to "lelê",
            "age" to 24
        )

        db.collection("user").document("leticia@gmail.com")
            .set(jogMap)
            .addOnSuccessListener({
                // Código se deu tudo certo
            })
            .addOnFailureListener({
                // Código se deu errado
            })
    }
}