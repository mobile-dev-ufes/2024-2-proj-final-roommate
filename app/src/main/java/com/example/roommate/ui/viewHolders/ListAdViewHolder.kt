package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.databinding.AdLineBinding
import com.example.roommate.data.model.AdModel
import com.example.roommate.viewModel.MyAdsViewModel

class ListAdViewHolder(
    private val binding: AdLineBinding,
    private val adViewModel: MyAdsViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun bindVH(ad: AdModel, onClickButton: (AdModel) -> Unit) {
        binding.adDescriptionTv.text = ad.title
        binding.adLocalTv.text = ad.localToString()
        binding.adPriceTv.text = ad.getValueString()

        // Call ViewModel function to load image
        adViewModel.getAdImage(ad.id.toString()) { imageUrl ->
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.error_profile)
                .into(binding.adImg)
        }

        binding.favoriteAdsHeart.setOnClickListener {
            onClickButton(ad)
        }

    }
}