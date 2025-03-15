package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.databinding.AdLineBinding
import com.example.roommate.data.model.AdModel
import java.text.NumberFormat
import java.util.Locale

class ListAdViewHolder(private val binding: AdLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bindVH(ad: AdModel){
            binding.adDescriptionTv.text = ad.title
            binding.adLocalTv.text = ad.localToString()

            // TODO Avaliar modularizar esse trecho
            val locale = Locale.getDefault()
            val currencyFormat = NumberFormat.getCurrencyInstance(locale)
            val formattedCurrency = currencyFormat.format(ad.rent_value)
            val text = "$formattedCurrency/ mês"

            binding.adPriceTv.text = text
        }
}