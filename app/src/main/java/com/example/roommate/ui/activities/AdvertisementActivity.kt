package com.example.roommate.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roommate.databinding.ActivityAdvertisementBinding

class AdvertisementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdvertisementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertisementBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}