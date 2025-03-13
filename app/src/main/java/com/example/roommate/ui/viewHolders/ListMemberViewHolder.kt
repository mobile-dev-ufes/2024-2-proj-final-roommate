package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.EntryRequestsLineBinding

class ListMemberViewHolder(private val binding: EntryRequestsLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bindVH(user: UserModel){
            binding.nameTv.text = user.name
            binding.bioTv.text = user.bio
        }
}