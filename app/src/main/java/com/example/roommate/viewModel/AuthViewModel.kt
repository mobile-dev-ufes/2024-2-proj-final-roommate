package com.example.roommate.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.data.repository.AuthRepository
import com.example.roommate.utils.authStatusEnum
import com.example.roommate.utils.statusEnum

/**
 * ViewModel responsável pela autenticação de usuários no aplicativo.
 *
 * O [AuthViewModel] gerencia o fluxo de autenticação de usuários, como o registro e login,
 * interagindo com o repositório de autenticação ([AuthRepository]). Ele mantém o estado atual
 * da autenticação do usuário, como se o processo foi bem-sucedido ou falhou, e expõe esse
 * estado através de um [MutableLiveData].
 *
 * @property authRepository Instância do [AuthRepository] responsável por gerenciar as
 * operações de autenticação com o repositório de dados.
 * @property status [MutableLiveData] que mantém o status atual da autenticação, que pode
 * ser em um estado de sucesso, falha, ou ainda não iniciado.
 */
class AuthViewModel : ViewModel() {
    // Instância do repositório responsável pela autenticação
    private val authRepository = AuthRepository()

    // Status da autenticação
    private var status = MutableLiveData<authStatusEnum>(authStatusEnum.NIL)

    /**
     * Retorna o [MutableLiveData] que contém o status da autenticação.
     *
     * O status pode ser um valor da enumeração [authStatusEnum], como [SUCCESS], [FAIL],
     * [WEAK_PASS], etc.
     *
     * @return [MutableLiveData] que contém o status da autenticação.
     */
    fun isRegistered(): MutableLiveData<authStatusEnum> {
        return status
    }

    /**
     * Método responsável por registrar um novo usuário.
     *
     * Esse método chama o repositório de autenticação para registrar um usuário no sistema
     * com as credenciais fornecidas. O resultado da operação é emitido através do [status].
     *
     * @param email O e-mail do usuário a ser registrado.
     * @param pass A senha do usuário a ser registrada.
     */
    fun registerUser(email: String, pass: String){
        authRepository.registerUser(email, pass, status)
    }

    /**
     * Método responsável por autenticar um usuário existente.
     *
     * Esse método chama o repositório de autenticação para autenticar o usuário com as
     * credenciais fornecidas. O resultado da operação é emitido através do [status].
     *
     * @param email O e-mail do usuário a ser autenticado.
     * @param pass A senha do usuário a ser autenticada.
     */
    fun authenticateUser(email: String, pass: String){
        authRepository.authUser(email, pass, status)
    }
}