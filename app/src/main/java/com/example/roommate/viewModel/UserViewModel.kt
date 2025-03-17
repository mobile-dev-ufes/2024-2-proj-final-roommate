package com.example.roommate.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.data.repository.UserRepository
import com.example.roommate.utils.statusEnum

/**
 * ViewModel responsável por gerenciar os dados relacionados ao usuário.
 *
 * O [UserViewModel] gerencia o estado do usuário atual, incluindo a obtenção de informações do
 * usuário,a criação de novos usuários, e a associação de grupos ao usuário. Ele interage com o
 * [UserRepository] para obter ou armazenar dados sobre o usuário, incluindo grupos e imagens
 * de perfil.
 * A classe expõe esses dados através de [LiveData], permitindo que a interface do usuário reaja
 * a mudanças de forma reativa.
 *
 * @property userRepository Instância do [UserRepository] para realizar operações relacionadas ao
 * usuário.
 * @property currentUser [MutableLiveData] que contém o modelo de dados do usuário atual.
 * @property status [MutableLiveData] que mantém o status da operação (ex: sucesso ou falha).
 * @property _groups [MutableLiveData] que contém a lista de grupos aos quais o usuário pertence.
 * @property groups [LiveData] que fornece acesso aos grupos do usuário de forma imutável.
 * @property _profileImageUrl [MutableLiveData] que contém a URL da imagem de perfil do usuário.
 * @property profileImageUrl [LiveData] que fornece acesso à URL da imagem de perfil de forma imutável.
 */
class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private var currentUser = MutableLiveData<UserModel>()
    private var status = MutableLiveData<statusEnum>(statusEnum.NIL)

    private val _groups = MutableLiveData<List<GroupModel>>()
    val groups: LiveData<List<GroupModel>> = _groups

    private val _profileImageUrl = MutableLiveData<String>()
    val profileImageUrl: LiveData<String> = _profileImageUrl

    /**
     * Retorna o status atual de registro ou autenticação do usuário.
     *
     * @return [MutableLiveData] contendo o status da operação (ex: sucesso ou falha).
     */
    fun isRegistered(): MutableLiveData<statusEnum> {
        return status
    }

    /**
     * Retorna o usuário atual.
     *
     * @return O modelo de dados do usuário atual. Se o usuário não estiver definido, retorna um usuário vazio.
     */
    fun getCurrentUser(): UserModel{
        return currentUser.value ?: UserModel()
    }

    /**
     * Registra um novo usuário.
     *
     * Este método armazena os dados do usuário atual e chama o repositório para realizar o registro do usuário.
     *
     * @param user O modelo de dados do usuário a ser registrado.
     */
    fun registerUser(user: UserModel) {
        currentUser.value = user
        userRepository.create(user, status)
    }

    /**
     * Obtém as informações de um usuário a partir do seu email.
     *
     * Este método busca as informações do usuário no repositório, utilizando o email para identificação.
     *
     * @param userEmail O email do usuário a ser obtido.
     */
    fun getUser(userEmail: String) {
        userRepository.get(userEmail, status, currentUser)
    }

    /**
     * Obtém os grupos aos quais o usuário pertence.
     *
     * Este método chama o repositório para buscar os grupos associados ao usuário.
     * A lista de grupos é atualizada na variável [groups].
     *
     * @param userId O ID do usuário para obter os grupos associados.
     */
    fun getGroupsForUser(userId: String) {
        userRepository.getGroupsForUser(userId) { userList ->
            _groups.postValue(userList)
        }
    }

    /**
     * Adiciona um grupo ao usuário.
     *
     * Este método chama o repositório para associar um grupo ao usuário.
     *
     * @param userId O ID do usuário ao qual o grupo será associado.
     * @param groupId O ID do grupo a ser adicionado ao usuário.
     */
    fun addGroupToUser(userId: String, groupId: String) {
        userRepository.addGroupToUser(userId, groupId)
    }

    /**
     * Carrega a imagem de perfil do usuário.
     *
     * Este método chama o repositório para obter a URL da imagem de perfil do usuário.
     * Em caso de sucesso, a URL é atualizada na variável [profileImageUrl]. Em caso de falha,
     * a exceção é registrada no log.
     *
     * @param userId O ID do usuário para obter a imagem de perfil.
     */
    fun loadProfileImage(userId: String) {
        userRepository.getProfileImage(
            userId = userId,
            onSuccess = { url -> _profileImageUrl.postValue(url) },
            onFailure = { exception -> Log.e("ProfileViewModel", "Failed to get image URL", exception) }
        )
    }
}