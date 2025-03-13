package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.FragmentGroupBinding
import com.example.roommate.ui.adapters.ListMemberAdapter
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FragmentGroup : Fragment(R.layout.fragment_group) {
    private lateinit var binding: FragmentGroupBinding
    private lateinit var adapter: ListMemberAdapter
    private var group: GroupModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentGroupBinding.inflate(inflater, container, false)

        group = arguments?.getSerializable("group") as? GroupModel

        adapter = ListMemberAdapter { user ->  // Corrected: Using ListMemberAdapter
            findNavController().navigate(R.id.action_fragmentGroup_to_fragmentVisitProfile)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

        binding.groupNameTv.text = group?.name
        binding.groupDescriptionTv.text = group?.description
        binding.groupQttMembersTv.text = "Members: ${group?.qttMembers}"  // Display members count

        // Apenas para testar o Recycle View
        adapter.insertMemberList(UserModel("Daniel1", "A vida é bela1"))
        adapter.insertMemberList(UserModel("Daniel2", "A vida é bela2"))
        adapter.insertMemberList(UserModel("Daniel3", "A vida é bela3"))

    }


}