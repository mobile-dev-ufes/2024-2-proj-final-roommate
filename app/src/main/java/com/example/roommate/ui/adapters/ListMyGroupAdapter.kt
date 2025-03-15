package com.example.roommate.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.MyGroupsLineBinding
import com.example.roommate.ui.viewHolders.ListMyGroupViewHolder

class ListMyGroupAdapter(private val onItemClick: (GroupModel) -> Unit) : RecyclerView.Adapter<ListMyGroupViewHolder>() {
    private var groupList: MutableList<GroupModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMyGroupViewHolder {
        val item = MyGroupsLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListMyGroupViewHolder(item)
    }

    override fun onBindViewHolder(holder: ListMyGroupViewHolder, position: Int) {
        holder.bindVH(groupList[position])

        holder.itemView.setOnClickListener {
            onItemClick(groupList[position])
        }
    }

    override fun getItemCount(): Int = groupList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateGroupList(list: MutableList<GroupModel>) {
        groupList.clear() // Avoid replacing the reference
        groupList.addAll(list)
        notifyDataSetChanged()
    }

    fun insertGroupList(group: GroupModel) {
        groupList.add(group)
        notifyItemInserted(groupList.size - 1) // Corrected index
    }
}