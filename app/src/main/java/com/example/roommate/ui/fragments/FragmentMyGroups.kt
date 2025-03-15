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
import com.example.roommate.ui.adapters.ListMyGroupAdapter
import com.example.roommate.ui.viewModels.GroupViewModel

class FragmentMyGroups : Fragment(R.layout.fragment_my_groups) {
    private lateinit var binding: FragmentMyGroupsBinding
    private lateinit var adapter: ListMyGroupAdapter
    private val groupViewModel: GroupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMyGroupsBinding.inflate(inflater, container, false)

        adapter = ListMyGroupAdapter { group ->
            val action = FragmentMyGroupsDirections
                .actionFragmentMyGroupsToFragmentGroup(group)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleListGroups.layoutManager = LinearLayoutManager(context)
        binding.recycleListGroups.adapter = adapter

        groupViewModel.fetchUserGroups("leticia@email.com")
        observerGroups()
    }

    private fun observerGroups() {
        groupViewModel.groups.observe(viewLifecycleOwner) { groups ->
            adapter.updateGroupList(groups.toMutableList())
        }
    }
}
