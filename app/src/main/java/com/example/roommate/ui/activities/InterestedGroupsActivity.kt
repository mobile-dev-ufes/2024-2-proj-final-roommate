package com.example.roommate.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roommate.databinding.ActivityEntryRequestsBinding
import com.example.roommate.databinding.ActivityInterestedGroupsBinding

class InterestedGroupsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterestedGroupsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterestedGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}