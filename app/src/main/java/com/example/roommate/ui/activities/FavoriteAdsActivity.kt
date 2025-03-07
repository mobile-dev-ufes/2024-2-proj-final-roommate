package com.example.roommate.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roommate.databinding.ActivityFavoriteAdsBinding

class FavoriteAdsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}