package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.FragmentGroupBinding
import com.example.roommate.ui.adapters.ListMemberAdapter
import com.example.roommate.ui.viewModels.GroupViewModel

class FragmentGroup : Fragment(R.layout.fragment_group) {
    private lateinit var binding: FragmentGroupBinding
    private lateinit var adapter: ListMemberAdapter
    private var argsGroup: GroupModel? = null
    private val groupViewModel: GroupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentGroupBinding.inflate(inflater, container, false)

        argsGroup = arguments?.getSerializable("group") as? GroupModel

        adapter = ListMemberAdapter { user ->
            val bundle = Bundle().apply {
                putSerializable("user", user)  // Passing GroupModel to FragmentGroup
            }
            findNavController().navigate(R.id.action_fragmentGroup_to_fragmentVisitProfile, bundle)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

        argsGroup?.let { group ->
            binding.groupNameTv.text = group.name
            binding.groupDescriptionTv.text = group.description
            binding.groupQttMembersTv.text = getString(R.string.show_members_qtt, group.qttMembers.toString())

            groupViewModel.getMembersFromGroup(group.id)
        } ?: run {
            Toast.makeText(context, "Error loading group data", Toast.LENGTH_SHORT).show()
        }

        observerGroups()
    }

    private fun observerGroups() {
        groupViewModel.members.observe(viewLifecycleOwner) { members ->
            adapter.updateMemberList(members.toMutableList())
        }
    }
}