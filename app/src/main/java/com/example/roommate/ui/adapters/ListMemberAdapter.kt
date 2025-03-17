package com.example.roommate.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.EntryRequestsLineBinding
import com.example.roommate.ui.viewHolders.ListMemberViewHolder
import com.example.roommate.viewModel.UserViewModel

class ListMemberAdapter(
    private val onClickItem: (UserModel) -> Unit,
    private val userViewModel: UserViewModel
): RecyclerView.Adapter<ListMemberViewHolder>() {
    private val memberList: MutableList<UserModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMemberViewHolder {
        val item = EntryRequestsLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListMemberViewHolder(item, userViewModel)
    }

    override fun onBindViewHolder(holder: ListMemberViewHolder, position: Int) {
        holder.bindVH(memberList[position])

        holder.itemView.setOnClickListener{
            onClickItem(memberList[position])
        }
    }

    override fun getItemCount(): Int = memberList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateMemberList(list: MutableList<UserModel>) {
        memberList.clear() // Avoid replacing the reference
        memberList.addAll(list)
        notifyDataSetChanged()
    }

    fun insertMemberList(user: UserModel){
        memberList.add(user)
        notifyItemInserted(memberList.count())
    }
}