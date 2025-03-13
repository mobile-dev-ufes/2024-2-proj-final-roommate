package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.FragmentSignup1Binding
import com.example.roommate.databinding.FragmentSignup2Binding

class FragmentSignup2 : Fragment(R.layout.fragment_signup1), View.OnClickListener {
    private lateinit var binding: FragmentSignup2Binding

    private val args: FragmentSignup2Args by navArgs()

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
        binding.signup2GoBtn.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.signup2_go_btn) {
            // TODO Acertar os tipos e modularizar
            val nameText = binding.nameEt.text.toString()
            val phoneText = binding.phoneEt.text.toString()
            val birthdayText = binding.birthdayEt.text.toString()

            val action = FragmentSignup2Directions.actionFragmentSignup2ToFragmentSignup3(
                UserModel(
                    email = args.userInfo.email,
                    name = nameText,
                    bio = null,
                    age = null,
                    sex = null,
                    phone = phoneText,
                    birthDate = birthdayText,
                    photo_uri = null
                )
            )
            findNavController().navigate(action)
        }
    }
}