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
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.databinding.FragmentGroupBinding
import com.example.roommate.ui.adapters.ListMemberAdapter
import com.example.roommate.utils.userManager
import com.example.roommate.viewModel.GroupViewModel
import com.example.roommate.viewModel.UserViewModel

class FragmentGroup : Fragment(R.layout.fragment_group) {
    private lateinit var binding: FragmentGroupBinding
    private lateinit var adapter: ListMemberAdapter
    private val groupViewModel: GroupViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val args: FragmentGroupArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentGroupBinding.inflate(inflater, container, false)

        adapter = ListMemberAdapter(
            onClickItem = { user ->
                val action = FragmentGroupDirections
                    .actionFragmentGroupToFragmentVisitProfile(user)
                findNavController().navigate(action)
            },
            userViewModel = userViewModel
        )

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

        val userEmail = userManager.user.email.toString()
        binding.groupGetInBtn.setOnClickListener {
            groupViewModel.groupEntryLogic(argsGroup.id, userEmail)
        }

        val groupId = args.group.id

        groupViewModel.groupImageUrl.observe(viewLifecycleOwner) { url ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.error_profile)
                .into(binding.profileImage)
        }

        groupViewModel.loadGroupImage(groupId)

        observerGroups()
    }

    private fun observerGroups() {
        groupViewModel.members.observe(viewLifecycleOwner) { members ->
            adapter.updateMemberList(members.toMutableList())
        }
    }
}