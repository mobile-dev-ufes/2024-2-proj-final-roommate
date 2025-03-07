package com.example.roommate.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roommate.databinding.ActivityEntryRequestsBinding

class EntryRequestsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEntryRequestsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}