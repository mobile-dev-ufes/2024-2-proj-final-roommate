package com.example.roommate.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.databinding.FragmentSignup2Binding
import com.example.roommate.databinding.FragmentSignup3Binding
import com.example.roommate.ui.activities.LoginActivity
import com.example.roommate.utils.statusEnum
import com.example.roommate.viewModel.UserViewModel

class FragmentSignup2 : Fragment(R.layout.fragment_signup2), View.OnClickListener {
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

        setUserInfo()

        binding.signup2GoBtn.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.signup2_go_btn) {
            args.userInfo.bio = binding.userBioEt.text.toString()

            val action = FragmentSignup2Directions.actionFragmentSignup2ToFragmentSignup3(args.userInfo)
            findNavController().navigate(action)
        }
    }

    private fun setUserInfo() {
        binding.usernameEt.setText(args.userInfo.name)
        binding.userPhoneEt.setText(args.userInfo.phone)
    }
}