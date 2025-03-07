package com.example.roommate.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roommate.databinding.ActivityMyAdsBinding

class MyAdsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}