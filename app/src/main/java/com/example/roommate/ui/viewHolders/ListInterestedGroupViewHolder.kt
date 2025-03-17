package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.InterestedGroupsLineBinding

/**
 * ViewHolder responsável por exibir os detalhes de um grupo interessado em uma linha dentro do
 * RecyclerView.
 *
 * O [ListInterestedGroupViewHolder] é utilizado para associar as informações de um [GroupModel] aos
 * elementos da interface dentro de uma célula do RecyclerView. Ele popula a interface com o nome do
 * grupo e a quantidade de membros.
 *
 * @property binding O objeto [InterestedGroupsLineBinding] que contém as referências dos elementos
 * da interface dentro do layout da célula, como o nome do grupo e a quantidade de membros.
 */
class ListInterestedGroupViewHolder(private val binding: InterestedGroupsLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * Método responsável por vincular os dados de um [GroupModel] aos elementos de UI no [binding].
     *
     * Esse método é chamado para configurar a interface do usuário com as informações específicas
     * de um grupo.
     * Ele preenche os campos do layout com o nome do grupo e a quantidade de membros.
     *
     * @param group O objeto [GroupModel] contendo as informações do grupo, como nome e quantidade de membros.
     */
     fun bindVH(group: GroupModel){
        binding.groupDescriptionTv.text = group.name

        val textQttMembers = "${group.qttMembers} membros"
        binding.groupQttMembersTv.text = textQttMembers
     }
}