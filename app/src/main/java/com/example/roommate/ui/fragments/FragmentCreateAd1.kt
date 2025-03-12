package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.roommate.R
import com.example.roommate.databinding.FragmentCreateAd1Binding

class FragmentCreateAd1 : Fragment(R.layout.fragment_create_ad1) {
    private lateinit var binding: FragmentCreateAd1Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentCreateAd1Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ad1ProceedBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentCreateAd1_to_fragmentCreateAd2)
        }
    }
}