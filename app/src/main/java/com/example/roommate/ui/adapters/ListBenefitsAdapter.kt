package com.example.roommate.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.databinding.BenefitsLineBinding
import com.example.roommate.ui.viewHolders.ListBenefitsViewHolder

class ListBenefitsAdapter : RecyclerView.Adapter<ListBenefitsViewHolder>() {
    private var benefitsList: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBenefitsViewHolder {
        val item = BenefitsLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListBenefitsViewHolder(item)
    }

    override fun onBindViewHolder(
        holder: ListBenefitsViewHolder, position: Int
    ) {
        holder.bindVH(benefitsList[position])
    }

    override fun getItemCount(): Int {
        return benefitsList.count()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateBenefitList(list: MutableList<String>){
        benefitsList = list
        notifyDataSetChanged()
    }

    fun insertBenefitList(benefit: String){
        benefitsList.add(benefit)
        notifyItemInserted(benefitsList.count())
    }
}