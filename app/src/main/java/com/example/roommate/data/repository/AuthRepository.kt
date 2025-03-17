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

/**
 * Repositório responsável por gerenciar operações de autenticação de usuários usando o Firebase
 * Authentication.
 *
 * A classe [AuthRepository] fornece métodos para registrar e autenticar usuários, além de lidar
 * com os diferentes status de autenticação, atualizando o [liveStatus] com o resultado da operação.
 *
 * @property auth Instância do [FirebaseAuth] usada para realizar operações de autenticação no
 * Firebase.
 */
class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    /**
     * Registra um novo usuário no Firebase Authentication utilizando o e-mail e senha fornecidos.
     *
     * O método adiciona um ouvinte para o processo de criação de usuário, que atualiza o status da operação
     * por meio do [liveStatus]. Ele pode retornar diferentes status, dependendo do resultado:
     * - [authStatusEnum.SUCCESS] em caso de sucesso.
     * - [authStatusEnum.WEAK_PASS] se a senha for considerada fraca.
     * - [authStatusEnum.COLLISION] se o e-mail já estiver registrado.
     * - [authStatusEnum.NETWORK] se ocorrer um erro de rede.
     * - [authStatusEnum.FAIL] para erros não identificados.
     *
     * @param email O e-mail do usuário a ser registrado.
     * @param pass A senha do usuário a ser registrada.
     * @param liveStatus O LiveData que será atualizado com o status da operação.
     */
    fun registerUser(email: String, pass: String, liveStatus: MutableLiveData<authStatusEnum>) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                // Registro bem-sucedido
                if (it.isSuccessful) {
                    liveStatus.value = authStatusEnum.SUCCESS
                }
            }
            .addOnFailureListener {
                when (it) {
                    // Senha fraca
                    is FirebaseAuthWeakPasswordException -> {
                        liveStatus.value = authStatusEnum.WEAK_PASS
                        Log.d("AUTH-FIREBASE", "Senha fraca")
                    }
                    // E-mail já cadastrado
                    is FirebaseAuthUserCollisionException -> {
                        liveStatus.value = authStatusEnum.COLLISION
                        Log.d("AUTH-FIREBASE", "Email já cadastrado")
                    }
                    // Erro de rede
                    is FirebaseNetworkException -> {
                        liveStatus.value = authStatusEnum.NETWORK
                        Log.d("AUTH-FIREBASE", "Erro na conexão com a rede")
                    }
                    // Falha não classificada
                    else -> {
                        liveStatus.value = authStatusEnum.FAIL
                        Log.d("AUTH-FIREBASE", "Erro não identificado")
                    }
                }
            }
    }

    /**
     * Autentica um usuário existente no Firebase Authentication com o e-mail e senha fornecidos.
     *
     * O método adiciona um ouvinte para o processo de autenticação, que atualiza o status da operação
     * por meio do [liveStatus]. Ele pode retornar diferentes status, dependendo do resultado:
     * - [authStatusEnum.SUCCESS] em caso de sucesso.
     * - [authStatusEnum.INVALID_USER] se o usuário não existir.
     * - [authStatusEnum.INVALID_CREDENTIAL] se a senha estiver incorreta.
     * - [authStatusEnum.NETWORK] se ocorrer um erro de rede.
     * - [authStatusEnum.FAIL] para erros não identificados.
     *
     * @param email O e-mail do usuário a ser autenticado.
     * @param pass A senha do usuário a ser autenticada.
     * @param liveStatus O LiveData que será atualizado com o status da operação.
     */
    fun authUser(email: String, pass: String, liveStatus: MutableLiveData<authStatusEnum>) {
        auth.signInWithEmailAndPassword(email, pass)
            // Autenticação bem-sucedida
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    liveStatus.value = authStatusEnum.SUCCESS
                }
            }
            .addOnFailureListener {
                when (it) {
                    // Usuário inválido
                    is FirebaseAuthInvalidUserException -> {
                        liveStatus.value = authStatusEnum.INVALID_USER
                        Log.d("AUTH-FIREBASE", "Usuário não existe")
                    }
                    // Credenciais inválidas
                    is FirebaseAuthInvalidCredentialsException -> {
                        liveStatus.value = authStatusEnum.INVALID_CREDENTIAL
                        Log.d("AUTH-FIREBASE", "Senha incorreta")
                    }
                    // Erro de rede
                    is FirebaseNetworkException -> {
                        liveStatus.value = authStatusEnum.NETWORK
                        Log.d("AUTH-FIREBASE", "Erro na conexão com a rede")
                    }
                    // Falha não classificada
                    else -> {
                        liveStatus.value = authStatusEnum.FAIL
                        Log.d("AUTH-FIREBASE", "Erro não identificado")
                    }
                }
            }
    }
}