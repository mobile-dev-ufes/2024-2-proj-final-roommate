package com.example.roommate.ui.viewHolders

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.MyGroupsLineBinding
import com.example.roommate.viewModel.GroupViewModel
import com.example.roommate.viewModel.UserViewModel

class ListMyGroupViewHolder(
    private val binding: MyGroupsLineBinding,
    private val groupViewModel: GroupViewModel
) : RecyclerView.ViewHolder(binding.root) {

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

