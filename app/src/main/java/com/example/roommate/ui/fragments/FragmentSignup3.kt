package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.roommate.R
import com.example.roommate.databinding.FragmentSignup3Binding

class FragmentSignup3 : Fragment(R.layout.fragment_signup3) {
    private lateinit var binding: FragmentSignup3Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSignup3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.finishSignupBtn.setOnClickListener({
            findNavController().navigate(R.id.action_fragmentSignup3_to_loginActivity)
        })
    }
}