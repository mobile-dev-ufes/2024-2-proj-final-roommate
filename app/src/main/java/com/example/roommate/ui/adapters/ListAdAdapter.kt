package com.example.roommate.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.AdModel
import com.example.roommate.databinding.AdLineBinding
import com.example.roommate.ui.viewHolders.ListAdViewHolder

class ListAdAdapter(val onClickItem: (AdModel) -> Unit) : RecyclerView.Adapter<ListAdViewHolder>() {
    private var adList: MutableList<AdModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdViewHolder {
        val item = AdLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListAdViewHolder(item)
    }

    override fun onBindViewHolder(
        holder: ListAdViewHolder, position: Int
    ) {
        holder.bindVH(adList[position])

        holder.itemView.setOnClickListener{
            onClickItem(adList[position])
        }
    }

    override fun getItemCount(): Int {
        return adList.count()
    }

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