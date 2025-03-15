package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.databinding.FragmentGroupBinding
import com.example.roommate.ui.adapters.ListMemberAdapter
import com.example.roommate.viewModel.GroupViewModel

class FragmentGroup : Fragment(R.layout.fragment_group) {
    private lateinit var binding: FragmentGroupBinding
    private lateinit var adapter: ListMemberAdapter
    private val groupViewModel: GroupViewModel by viewModels()
    private val args: FragmentGroupArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentGroupBinding.inflate(inflater, container, false)

        adapter = ListMemberAdapter { user ->
            val action = FragmentGroupDirections
                .actionFragmentGroupToFragmentVisitProfile(user) // ✅ Use Safe Args
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

        val argsGroup = args.group

        binding.groupNameTv.text = argsGroup.name
        binding.groupDescriptionTv.text = argsGroup.description
        binding.groupQttMembersTv.text = getString(R.string.show_members_qtt, argsGroup.qttMembers.toString())

        groupViewModel.getMembersFromGroup(argsGroup.id)

        observerGroups()
    }

    private fun observerGroups() {
        groupViewModel.members.observe(viewLifecycleOwner) { members ->
            adapter.updateMemberList(members.toMutableList())
        }
    }
}