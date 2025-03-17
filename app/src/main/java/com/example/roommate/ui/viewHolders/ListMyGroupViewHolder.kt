package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.MyGroupsLineBinding


/**
 * ViewHolder responsável por exibir as informações de um grupo dentro de uma linha no RecyclerView.
 *
 * O [ListMyGroupViewHolder] é utilizado para associar os dados de um [GroupModel] aos elementos
 * da interface dentro de uma célula do RecyclerView. Ele popula a interface com o nome do grupo, a
 * quantidade de membros e a quantidade de notificações.
 *
 * @property binding O objeto [MyGroupsLineBinding] que contém as referências dos elementos da
 * interface dentro do layout da célula, como o nome do grupo, a quantidade de membros e notificações.
 */
class ListMyGroupViewHolder(private val binding: MyGroupsLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * Método responsável por vincular os dados de um [GroupModel] aos elementos de UI no [binding].
     *
     * Esse método é chamado para configurar a interface do usuário com as informações específicas
     * de um grupo.
     * Ele preenche os campos do layout com o nome do grupo, a quantidade de membros e notificações.
     *
     * @param group O objeto [GroupModel] contendo as informações do grupo, como nome, quantidade
     * de membros e quantidade de notificações.
     */
    fun bindVH(group: GroupModel){
        binding.groupDescriptionTv.text = group.name

        val textQttMembers = "${group.qttMembers} membros"
        binding.groupQttMembersTv.text = textQttMembers

        val textQttNotifications = group.qttNotifications.toString()
        binding.groupQttNotificationsTv.text = textQttNotifications
    }
}