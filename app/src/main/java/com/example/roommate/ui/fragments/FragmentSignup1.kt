package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.roommate.R
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.FragmentSignup1Binding
import com.example.roommate.utils.authStatusEnum
import com.example.roommate.viewModel.AuthViewModel

class FragmentSignup1 : Fragment(R.layout.fragment_signup1), View.OnClickListener {
    private lateinit var binding: FragmentSignup1Binding
    private lateinit var authVM: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentSignup1Binding.inflate(inflater, container, false)
        authVM = ViewModelProvider(this)[AuthViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()

        binding.signup1GoBtn.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.signup1_go_btn && checkFields()) {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()

            authVM.registerUser(email, pass)
        }
    }

    private fun setObserver(){
        authVM.isRegistered().observe(viewLifecycleOwner){ status ->
            when(status){
                authStatusEnum.SUCCESS ->{
                    val action = FragmentSignup1Directions.actionFragmentSignup1ToFragmentSignup2(
                        UserModel(
                            email = binding.emailEt.text.toString(),
                            name = null,
                            bio = null,
                            sex = null,
                            phone = null,
                            birthDate = null,
                            photo_uri = null
                        )
                    )
                    findNavController().navigate(action)
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
                    Toast.makeText(context, "Ocorreu um erro! Tente novamente.", Toast.LENGTH_SHORT).show()
                }
                else -> {}
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
}