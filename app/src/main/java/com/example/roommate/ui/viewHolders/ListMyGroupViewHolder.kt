package com.example.roommate.ui.viewHolders

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.MyGroupsLineBinding
import com.example.roommate.viewModel.GroupViewModel
import com.example.roommate.viewModel.UserViewModel

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
class ListMyGroupViewHolder(
    private val binding: MyGroupsLineBinding,
    private val groupViewModel: GroupViewModel
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Método responsável por vincular os dados de um [UserModel] aos elementos de UI no [binding].
     *
     * Esse método é chamado para configurar a interface do usuário com as informações específicas
     * de um usuário.
     * Ele preenche os campos do layout com o nome e a bio do usuário.
     *
     * @param user O objeto [UserModel] contendo as informações do usuário, como nome e bio.
     */
    fun bindVH(group: GroupModel) {
        binding.groupDescriptionTv.text = group.name

        val textQttMembers = "${group.qttMembers} membros"
        binding.groupQttMembersTv.text = textQttMembers

        val textQttNotifications = group.qttNotifications.toString()
        binding.groupQttNotificationsTv.text = textQttNotifications

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