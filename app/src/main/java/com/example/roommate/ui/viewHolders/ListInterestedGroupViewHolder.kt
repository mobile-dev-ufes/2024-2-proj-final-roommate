package com.example.roommate.ui.viewHolders

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.InterestedGroupsLineBinding
import com.example.roommate.viewModel.GroupViewModel

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
class ListInterestedGroupViewHolder(
    private val binding: InterestedGroupsLineBinding,
    private val groupViewModel: GroupViewModel
) : RecyclerView.ViewHolder(binding.root) {

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


        // Load image asynchronously from Firebase Storage
        println("GROUPID: ${group.id}")
        groupViewModel.getGroupImage(group.id,
            onSuccess = { imageUrl ->
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.profile_placeholder)
                    .error(R.drawable.error_profile)
                    .into(binding.groupImage)
            },
            onFailure = { exception ->
                Log.e("GroupViewHolder", "Error loading image for group ${group.id}", exception)
                Glide.with(binding.root.context)
                    .load(R.drawable.error_profile)
                    .into(binding.groupImage)
            }
        )

     }
}