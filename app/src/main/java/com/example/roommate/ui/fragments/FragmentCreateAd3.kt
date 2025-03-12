package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.roommate.R
import com.example.roommate.databinding.FragmentCreateAd3Binding

class FragmentCreateAd3 : Fragment(R.layout.fragment_create_ad3) {
    private lateinit var binding: FragmentCreateAd3Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentCreateAd3Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ad3FinishRegistration.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.fragmentMyAds, true) // Clears back stack up to fragmentMyAds
                .build()

            findNavController().navigate(
                R.id.action_fragmentCreateAd3_to_fragmentMyAds,
                null,
                navOptions
            )
        }
    }
}