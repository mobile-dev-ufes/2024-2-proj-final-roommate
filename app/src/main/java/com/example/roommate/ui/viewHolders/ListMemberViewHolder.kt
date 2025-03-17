package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.EntryRequestsLineBinding
import com.example.roommate.viewModel.UserViewModel

class ListMemberViewHolder(
    private val binding: EntryRequestsLineBinding,
    private val userViewModel: UserViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun bindVH(user: UserModel) {
        binding.nameTv.text = user.name
        binding.bioTv.text = user.bio

        // Call ViewModel function to load image
        userViewModel.getProfileImage(user.email.toString()) { imageUrl ->
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.error_profile)
                .into(binding.groupImage)
        }
    }
}