package com.example.roommate.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.roommate.R
import com.example.roommate.databinding.ActivitySignupBinding
import com.example.roommate.ui.fragments.FragmentSignup1
import com.example.roommate.ui.fragments.FragmentSignup2
import com.example.roommate.ui.fragments.FragmentSignup3

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<FragmentSignup3>(R.id.fragment_container_signup)
        }
    }
}