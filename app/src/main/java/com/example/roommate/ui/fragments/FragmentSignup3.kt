package com.example.roommate.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.databinding.FragmentSignup3Binding
import com.example.roommate.ui.activities.LoginActivity
import com.example.roommate.utils.statusEnum
import com.example.roommate.viewModel.SignUpViewModel

class FragmentSignup3 : Fragment(R.layout.fragment_signup3), View.OnClickListener {
    private lateinit var binding: FragmentSignup3Binding
    private lateinit var signUpVM: SignUpViewModel

    private val args: FragmentSignup3Args by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentSignup3Binding.inflate(inflater, container, false)
        signUpVM = ViewModelProvider(this)[SignUpViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
        setUserInfo()

        binding.finishSignupBtn.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.finish_signup_btn) {
            args.userInfo.bio = binding.userBioEt.text.toString()
            signUpVM.registerUser(args.userInfo)

            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setUserInfo() {
        binding.usernameEt.setText(args.userInfo.name)
        binding.userPhoneEt.setText(args.userInfo.phone)
    }

    private fun setObserver() {
        signUpVM.isRegistered().observe(viewLifecycleOwner) { status ->
            when (status) {
                statusEnum.SUCCESS -> Toast.makeText(
                    requireContext(),
                    "Cadastro realizado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()

                statusEnum.FAIL -> Toast.makeText(
                    requireContext(),
                    "Ocorreu um erro! Tente novamente.",
                    Toast.LENGTH_SHORT
                ).show()

                else -> UInt
            }
        }
    }
}