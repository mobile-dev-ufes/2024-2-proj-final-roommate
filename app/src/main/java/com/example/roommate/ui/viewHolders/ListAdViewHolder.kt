package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.R
import com.example.roommate.databinding.AdLineBinding
import com.example.roommate.data.model.AdModel
import java.text.NumberFormat
import java.util.Locale

class ListAdViewHolder(private val binding: AdLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindVH(ad: AdModel, onClickButton: (AdModel) -> Unit) {
        binding.adDescriptionTv.text = ad.title
        binding.adLocalTv.text = ad.localToString()
        binding.adPriceTv.text = ad.getValueString()

        binding.favoriteAdsHeart.setOnClickListener {
            onClickButton(ad)
        }

    }
}