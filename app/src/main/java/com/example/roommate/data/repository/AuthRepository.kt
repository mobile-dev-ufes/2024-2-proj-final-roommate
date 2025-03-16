package com.example.roommate.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.roommate.utils.authStatusEnum
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    fun registerUser(email: String, pass: String, liveStatus: MutableLiveData<authStatusEnum>) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    liveStatus.value = authStatusEnum.SUCCESS
                }
            }
            .addOnFailureListener {
                when (it) {
                    is FirebaseAuthWeakPasswordException -> {
                        liveStatus.value = authStatusEnum.WEAK_PASS
                        Log.d("AUTH-FIREBASE", "Senha fraca")
                    }

                    is FirebaseAuthUserCollisionException -> {
                        liveStatus.value = authStatusEnum.COLLISION
                        Log.d("AUTH-FIREBASE", "Email já cadastrado")
                    }

                    is FirebaseNetworkException -> {
                        liveStatus.value = authStatusEnum.NETWORK
                        Log.d("AUTH-FIREBASE", "Erro na conexão com a rede")
                    }

                    else -> {
                        liveStatus.value = authStatusEnum.FAIL
                        Log.d("AUTH-FIREBASE", "Erro não identificado")
                    }
                }
            }
    }

    fun authUser(email: String, pass: String, liveStatus: MutableLiveData<authStatusEnum>) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    liveStatus.value = authStatusEnum.SUCCESS
                }
            }
            .addOnFailureListener {
                when (it) {
                    is FirebaseAuthInvalidUserException -> {
                        liveStatus.value = authStatusEnum.INVALID_USER
                        Log.d("AUTH-FIREBASE", "Usuário não existe")
                    }

                    is FirebaseAuthInvalidCredentialsException -> {
                        liveStatus.value = authStatusEnum.INVALID_CREDENTIAL
                        Log.d("AUTH-FIREBASE", "Senha incorreta")
                    }

                    is FirebaseNetworkException -> {
                        liveStatus.value = authStatusEnum.NETWORK
                        Log.d("AUTH-FIREBASE", "Erro na conexão com a rede")
                    }

                    else -> {
                        liveStatus.value = authStatusEnum.FAIL
                        Log.d("AUTH-FIREBASE", "Erro não identificado")
                    }
                }
            }
    }
}