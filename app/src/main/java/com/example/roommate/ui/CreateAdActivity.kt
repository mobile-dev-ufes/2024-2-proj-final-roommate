package com.example.roommate.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.roommate.R
import com.example.roommate.databinding.ActivityCreateAdBinding
import androidx.fragment.app.commit
import androidx.fragment.app.add

class CreateAdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<FragmentCreateAd3>(R.id.fragment_create_ad)
        }

    }
}