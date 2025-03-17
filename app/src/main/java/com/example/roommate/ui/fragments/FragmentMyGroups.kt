package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.databinding.FragmentMyGroupsBinding
import com.example.roommate.ui.adapters.ListMemberAdapter
import com.example.roommate.ui.adapters.ListMyGroupAdapter
import com.example.roommate.utils.userManager
import com.example.roommate.viewModel.GroupViewModel
import com.example.roommate.viewModel.UserViewModel

class FragmentMyGroups : Fragment(R.layout.fragment_my_groups) {
    private lateinit var binding: FragmentMyGroupsBinding
    private lateinit var adapter: ListMyGroupAdapter
    private val userViewModel: UserViewModel by viewModels()
    private val groupViewModel: GroupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMyGroupsBinding.inflate(inflater, container, false)


        adapter = ListMyGroupAdapter(
            onClickItem = { group ->
                val action = FragmentMyGroupsDirections
                    .actionFragmentMyGroupsToFragmentGroup(group)
                findNavController().navigate(action)
            },
            groupViewModel = groupViewModel
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleListGroups.layoutManager = LinearLayoutManager(context)
        binding.recycleListGroups.adapter = adapter

        userViewModel.getGroupsForUser(userManager.user.email.toString())
        observerGroups()
    }

    private fun observerGroups() {
        userViewModel.groups.observe(viewLifecycleOwner) { groups ->
            adapter.updateGroupList(groups.toMutableList())
        }
    }
}
