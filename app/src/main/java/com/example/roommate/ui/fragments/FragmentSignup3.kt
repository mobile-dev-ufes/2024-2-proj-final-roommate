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
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.FragmentSignup1Binding
import com.example.roommate.databinding.FragmentSignup3Binding
import com.example.roommate.ui.activities.LoginActivity
import com.example.roommate.utils.authStatusEnum
import com.example.roommate.utils.statusEnum
import com.example.roommate.viewModel.AuthViewModel
import com.example.roommate.viewModel.UserViewModel

class FragmentSignup3 : Fragment(R.layout.fragment_signup3), View.OnClickListener {
    private lateinit var binding: FragmentSignup3Binding

    private lateinit var signUpVM: UserViewModel
    private lateinit var authVM: AuthViewModel

    private val args: FragmentSignup3Args by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentSignup3Binding.inflate(inflater, container, false)

        signUpVM = ViewModelProvider(this)[UserViewModel::class.java]
        authVM = ViewModelProvider(this)[AuthViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()

        binding.finishSignUp.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.finish_sign_up && checkFields()) {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()

            args.userInfo.email = email

            authVM.registerUser(email, pass)
        }
    }

    private fun setObserver(){
        authVM.isRegistered().observe(viewLifecycleOwner){ status ->
            when(status){
                authStatusEnum.SUCCESS ->{
                    signUpVM.registerUser(args.userInfo)
                }
                authStatusEnum.WEAK_PASS ->{
                    Toast.makeText(context, "Senha fraca! A senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                }
                authStatusEnum.COLLISION ->{
                    Toast.makeText(context, "Usuário já cadastrado!", Toast.LENGTH_SHORT).show()
                }
                authStatusEnum.NETWORK ->{
                    Toast.makeText(context, "Verfique sua conexão com a internet.", Toast.LENGTH_SHORT).show()
                }
                authStatusEnum.FAIL ->{
                    Toast.makeText(context, "Verifique se o e-mail tem formato válido.", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        signUpVM.isRegistered().observe(viewLifecycleOwner) { status ->
            when (status) {
                statusEnum.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        "Cadastro realizado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigate()
                }

                statusEnum.FAIL -> {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro! Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigate()
                }

                else -> UInt
            }
        }
    }

    private fun checkFields(): Boolean{
        if (binding.emailEt.text.isEmpty() ||
            binding.confirmEmailEt.text.isEmpty() ||
            binding.passEt.text.isEmpty() ||
            binding.confirmPassEt.text.isEmpty()) {

            Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()

            return false
        }
        if (binding.emailEt.text.toString() != binding.confirmEmailEt.text.toString()){
            Toast.makeText(context, "Os e-mails não coincidem.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.passEt.text.toString() != binding.confirmPassEt.text.toString()){
            Toast.makeText(context, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun navigate(){
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
}