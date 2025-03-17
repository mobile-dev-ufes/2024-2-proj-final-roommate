package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.MyGroupsLineBinding

class ListMyGroupViewHolder(private val binding: MyGroupsLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bindVH(group: GroupModel){
            binding.groupDescriptionTv.text = group.name

            // TODO Avaliar modularizar
            val textQttMembers = "${group.qttMembers} membros"
            binding.groupQttMembersTv.text = textQttMembers

            val textQttNotifications = group.qttNotifications.toString()
            binding.groupQttNotificationsTv.text = textQttNotifications
        }
}