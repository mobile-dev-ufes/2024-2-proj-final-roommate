package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.EntryRequestsLineBinding

/**
 * ViewHolder responsável por exibir as informações de um usuário (membro) dentro de uma linha no
 * RecyclerView.
 *
 * O [ListMemberViewHolder] é utilizado para associar os dados de um [UserModel] aos elementos da
 * interface dentro de uma célula do RecyclerView. Ele popula a interface com o nome e a bio do usuário.
 *
 * @property binding O objeto [EntryRequestsLineBinding] que contém as referências dos elementos
 * da interface dentro do layout da célula, como o nome do usuário e a sua bio.
 */
class ListMemberViewHolder(private val binding: EntryRequestsLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * Método responsável por vincular os dados de um [UserModel] aos elementos de UI no [binding].
     *
     * Esse método é chamado para configurar a interface do usuário com as informações específicas
     * de um usuário.
     * Ele preenche os campos do layout com o nome e a bio do usuário.
     *
     * @param user O objeto [UserModel] contendo as informações do usuário, como nome e bio.
     */
    fun bindVH(user: UserModel){
        binding.nameTv.text = user.name
        binding.bioTv.text = user.bio
    }
}