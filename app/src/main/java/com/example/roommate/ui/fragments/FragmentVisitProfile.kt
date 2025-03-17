package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.databinding.FragmentVisitProfileBinding
import com.example.roommate.viewModel.UserViewModel

class FragmentVisitProfile : Fragment(R.layout.fragment_visit_profile) {
    private lateinit var binding: FragmentVisitProfileBinding
    private val args: FragmentVisitProfileArgs by navArgs()
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentVisitProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = UserViewModel()

        val argsUser = args.user

        binding.usernameTv.text = argsUser.name
        binding.userBioTv.text = argsUser.bio
        binding.userPhoneTv.text = argsUser.phone

        val userId = argsUser.email.toString()

        userViewModel.profileImageUrl.observe(viewLifecycleOwner) { url ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.error_profile)
                .into(binding.profileImage)
        }

        userViewModel.loadProfileImage(userId)
    }
}