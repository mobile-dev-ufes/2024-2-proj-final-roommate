package com.example.roommate.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roommate.databinding.ActivitySignupBinding

/**
 * Activity responsável pelo processo de cadastro de usuário.
 *
 * A [SignUpActivity] fornece a interface de cadastro pelos fragments associados a ela, onde o
 *  usuário pode inserir suas informações para se registrar na aplicação.
 * A Activity usa [ActivitySignupBinding] para gerenciar os elementos da interface de usuário.
 */
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}