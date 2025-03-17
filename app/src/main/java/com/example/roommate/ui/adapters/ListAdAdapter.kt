package com.example.roommate.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.AdModel
import com.example.roommate.databinding.AdLineBinding
import com.example.roommate.ui.viewHolders.ListAdViewHolder
import com.example.roommate.viewModel.MyAdsViewModel

class ListAdAdapter(
    private val onClickItem: (AdModel) -> Unit,
    private val onClickButton: (AdModel) -> Unit,
    private val adViewModel: MyAdsViewModel
) : RecyclerView.Adapter<ListAdViewHolder>() {
    private var adList: MutableList<AdModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdViewHolder {
        val item = AdLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListAdViewHolder(item, adViewModel)
    }

    override fun onBindViewHolder(holder: ListAdViewHolder, position: Int) {
        val ad = adList[position]
        holder.bindVH(ad, onClickButton)

        holder.itemView.setOnClickListener {
            onClickItem(ad)
        }
    }

    override fun getItemCount(): Int = adList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdList(list: MutableList<AdModel>){
        adList = list
        notifyDataSetChanged()
    }

    fun insertAdList(ad: AdModel){
        adList.add(ad)
        notifyItemInserted(adList.count())
    }
}