package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.roommate.R
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.FragmentVisitProfileBinding

class FragmentVisitProfile : Fragment(R.layout.fragment_visit_profile) {
    private lateinit var binding: FragmentVisitProfileBinding
    private lateinit var argsUser: UserModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentVisitProfileBinding.inflate(inflater, container, false)

        argsUser = arguments?.getSerializable("user") as UserModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.usernameTv.text = argsUser.name
        binding.userBioTv.text = argsUser.bio
        binding.userPhoneTv.text = argsUser.phone
    }
}