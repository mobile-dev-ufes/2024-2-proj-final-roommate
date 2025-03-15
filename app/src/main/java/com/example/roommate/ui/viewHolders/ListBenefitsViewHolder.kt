package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.databinding.BenefitsLineBinding

class ListBenefitsViewHolder(private val binding: BenefitsLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindVH(benefit: String) {
        binding.contentTv.text = benefit
    }
}