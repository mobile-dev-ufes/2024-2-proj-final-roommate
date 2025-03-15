package com.example.roommate.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.ActivityLoginBinding
import com.example.roommate.utils.statusEnum
import com.example.roommate.utils.userManager
import com.example.roommate.viewModel.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userVM: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userVM = ViewModelProvider(this)[UserViewModel::class.java]
        setObserver()

        binding.signUpTv.setOnClickListener({
            startActivity((Intent(this, SignUpActivity::class.java)))
        })

        binding.signInBtn.setOnClickListener({
            if (checkFields() && authenticate()) {
                userVM.getUser(binding.emailEt.text.toString())
            }
        })
    }

    private fun navigate(){
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun setObserver() {
        userVM.isRegistered().observe(this) { status ->
            when (status) {
                statusEnum.SUCCESS -> {
                    val currentUser = userVM.getCurrentUser()

                    userManager.user = UserModel(
                        email = currentUser.email,
                        name = currentUser.name,
                        bio = currentUser.bio,
                        sex = currentUser.sex,
                        phone = currentUser.phone,
                        birthDate = currentUser.birthDate,
                        photo_uri = currentUser.photo_uri
                    )
                    navigate()
                }

                statusEnum.FAIL -> {
                    Toast.makeText(
                        this,
                        "Usuário não existe",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> UInt
            }
        }
    }

    private fun authenticate(): Boolean {
        return true
    }

    private fun checkFields(): Boolean {
        if (binding.emailEt.text.isEmpty() || binding.passEt.text.isEmpty()) {
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