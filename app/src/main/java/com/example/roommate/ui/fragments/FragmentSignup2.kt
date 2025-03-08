package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.roommate.R
import com.example.roommate.databinding.FragmentSignup1Binding
import com.example.roommate.databinding.FragmentSignup2Binding

class FragmentSignup2 : Fragment(R.layout.fragment_signup1) {
    private lateinit var binding: FragmentSignup2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentSignup2Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signup2GoBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSignup2_to_fragmentSignup3)
        }
    }
}