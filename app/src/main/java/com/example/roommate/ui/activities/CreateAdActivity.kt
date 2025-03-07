package com.example.roommate.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.roommate.R
import com.example.roommate.databinding.ActivityCreateAdBinding
import com.example.roommate.ui.fragments.FragmentCreateAd3

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