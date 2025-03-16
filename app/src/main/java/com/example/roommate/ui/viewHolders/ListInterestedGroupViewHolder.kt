package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.InterestedGroupsLineBinding

class ListInterestedGroupViewHolder(private val binding: InterestedGroupsLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bindVH(group: GroupModel){
            binding.groupDescriptionTv.text = group.name

            val textQttMembers = "${group.qttMembers} membros"
            binding.groupQttMembersTv.text = textQttMembers

            println(textQttMembers)
        }
}