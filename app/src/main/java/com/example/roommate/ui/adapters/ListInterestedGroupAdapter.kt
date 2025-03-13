package com.example.roommate.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.InterestedGroupsLineBinding
import com.example.roommate.ui.viewHolders.ListInterestedGroupViewHolder

class ListInterestedGroupAdapter(private val onClickItem: (GroupModel) -> Unit) :
    RecyclerView.Adapter<ListInterestedGroupViewHolder>() {
        private val groupList: MutableList<GroupModel> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListInterestedGroupViewHolder {
        val item =
            InterestedGroupsLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListInterestedGroupViewHolder(item)
    }

    override fun onBindViewHolder(holder: ListInterestedGroupViewHolder, position: Int) {
        holder.bindVH(groupList[position])
    }

    override fun getItemCount(): Int {
        return groupList.count()
    }

    fun insertGroupList(group: GroupModel){
        groupList.add(group)
        notifyItemInserted(groupList.count())
    }
}