package com.example.roommate.utils

import com.google.firebase.auth.FirebaseAuth

object AuthManager {
    private val auth = FirebaseAuth.getInstance()

    fun registerUser(){
        auth.createUserWithEmailAndPassword("auth@email.com", "minha_senha")
    }
}