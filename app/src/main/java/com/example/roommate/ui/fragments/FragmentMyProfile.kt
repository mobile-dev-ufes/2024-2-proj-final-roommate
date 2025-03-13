package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.roommate.R
import com.example.roommate.databinding.FragmentMyProfileBinding

class FragmentMyProfile : Fragment(R.layout.fragment_signup1) {
    private lateinit var binding: FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myAdsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMyProfile_to_fragmentMyAds)
        }
        binding.myFavoriteAdsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMyProfile_to_fragmentFavoriteAds)
        }
    }
}