package com.example.roommate.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.FragmentSignup3Binding
import com.example.roommate.ui.activities.LoginActivity
import com.google.firebase.firestore.FirebaseFirestore

class FragmentSignup3 : Fragment(R.layout.fragment_signup3), View.OnClickListener{
    private lateinit var binding: FragmentSignup3Binding

    private val args: FragmentSignup3Args by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSignup3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.finishSignupBtn.setOnClickListener(this)
    }

    override fun onClick(view: View){
        sendToFirestore()

        if(view.id == R.id.finish_signup_btn){
            startActivity(Intent(context, LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun sendToFirestore(){
        val db = FirebaseFirestore.getInstance()

        val userMap = hashMapOf(
            "name" to args.userInfo.name,
            "bio" to args.userInfo.bio,
            "age" to args.userInfo.age,
            "sex" to args.userInfo.sex,
            "phone" to args.userInfo.phone,
            "birthDate" to args.userInfo.birthDate,
            "photo_uri" to args.userInfo.photo_uri
        )

        db.collection("user").document(args.userInfo.email!!)
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(context, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(context, "Erro ao cadastrar usuário!", Toast.LENGTH_SHORT).show()
            }
    }
}