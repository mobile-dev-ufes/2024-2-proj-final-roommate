package com.example.roommate.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.data.repository.GroupRepository
import com.google.firebase.firestore.firestore

/**
 * ViewModel responsável pela lógica de interação com grupos no aplicativo.
 *
 * O [GroupViewModel] gerencia as operações relacionadas aos grupos, como registro de grupos,
 * obtenção de membros e imagens do grupo, além de adicionar membros a grupos. Ele interage com
 * o [GroupRepository] para realizar as operações e expõe os dados para a interface do usuário
 * através de [LiveData] para que a UI reaja a mudanças.
 *
 * @property userViewModel Instância do [UserViewModel] para lidar com operações de usuários.
 * @property groupRepository Instância do [GroupRepository] para realizar operações relacionadas a grupos.
 * @property _groups [MutableLiveData] que contém a lista de grupos.
 * @property groups [LiveData] que fornece acesso à lista de grupos.
 * @property _members [MutableLiveData] que contém a lista de membros de um grupo.
 * @property members [LiveData] que fornece acesso à lista de membros de um grupo.
 * @property _groupImageUrl [MutableLiveData] que contém a URL da imagem do grupo.
 * @property groupImageUrl [LiveData] que fornece acesso à URL da imagem do grupo.
 */
class GroupViewModel : ViewModel() {
    private val userViewModel = UserViewModel() // Instância do ViewModel para usuários
    private val groupRepository = GroupRepository()  // Instância do repositório de grupos

    private val _groups = MutableLiveData<List<GroupModel>>() // Lista de grupos
    val groups: LiveData<List<GroupModel>> = _groups // LiveData para acessar a lista de membros

    private val _members = MutableLiveData<List<UserModel>>() // Lista de membros do grupo
    val members: LiveData<List<UserModel>> = _members // LiveData para acessar a lista de membros

    private val _groupImageUrl = MutableLiveData<String>() // URL da imagem do grupo
    val groupImageUrl: LiveData<String> = _groupImageUrl // LiveData para acessar a URL da imagem do grupo

    /**
     * Registra um novo grupo no sistema.
     *
     * Esse método utiliza o repositório de grupos para registrar um novo grupo. Em caso de sucesso,
     * o método [onSuccess] será chamado. Em caso de falha, o método [onFailure] será chamado com
     * a exceção gerada.
     *
     * @param group O grupo a ser registrado.
     * @param onSuccess Função que será chamada em caso de sucesso.
     * @param onFailure Função que será chamada em caso de falha.
     */
    fun registerGroup(group: GroupModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        groupRepository.registerGroup(group, onSuccess, onFailure)
    }

    /**
     * Obtém os membros de um grupo específico.
     *
     * Esse método chama o repositório de grupos para recuperar a lista de membros de um grupo
     * e atualiza o [LiveData] de membros com os dados obtidos.
     *
     * @param groupId O ID do grupo cujos membros devem ser recuperados.
     */
    fun getMembersFromGroup(groupId: String) {
        groupRepository.getMembersFromGroup(groupId) { userList ->
            _members.postValue(userList)
        }
    }

    /**
     * Lógica de entrada de um usuário em um grupo.
     *
     * Esse método adiciona um usuário a um grupo e também atualiza as informações do grupo
     * no perfil do usuário.
     *
     * @param groupId O ID do grupo ao qual o usuário será adicionado.
     * @param userId O ID do usuário a ser adicionado ao grupo.
     */
    fun groupEntryLogic(groupId: String, userId: String) {
        groupRepository.addGroupMember(groupId, userId)
        userViewModel.addGroupToUser(userId, groupId)
    }

    /**
     * Carrega a imagem do grupo.
     *
     * Esse método chama o repositório de grupos para recuperar a URL da imagem do grupo
     * e atualiza o [LiveData] de imagem do grupo com a URL obtida.
     *
     * @param groupId O ID do grupo cuja imagem será carregada.
     */
    fun loadGroupImage(groupId: String) {
        groupRepository.getGroupImage(
            groupId = groupId,
            onSuccess = { url -> _groupImageUrl.postValue(url) },
            onFailure = { exception -> Log.e("groupViewModel", "Failed to get image URL", exception) }
        )
    }

    fun getGroupImage(groupId: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        groupRepository.getGroupImage(groupId, onSuccess, onFailure)
    }
}
