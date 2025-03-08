package com.example.roommate.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roommate.databinding.ActivityVisitProfileBinding

class VisitProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVisitProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}