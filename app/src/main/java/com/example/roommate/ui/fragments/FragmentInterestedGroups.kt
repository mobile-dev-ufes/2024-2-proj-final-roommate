package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.FragmentInterestedGroupsBinding
import com.example.roommate.ui.adapters.ListInterestedGroupAdapter

class FragmentInterestedGroups : Fragment(R.layout.fragment_interested_groups) {
    private lateinit var binding: FragmentInterestedGroupsBinding
    private lateinit var adapter: ListInterestedGroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentInterestedGroupsBinding.inflate(inflater, container, false)
        adapter = ListInterestedGroupAdapter{
            findNavController().navigate(R.id.action_fragmentInterestedGroups_to_fragmentGroup)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createGroupBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentInterestedGroups_to_dialogCreateGroup)
        }

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

//        // Apenas para teste do Recycle View
//        adapter.insertGroupList(GroupModel("Grupo1", "Meu teste de grupo3", 3, 6, null))
//        adapter.insertGroupList(GroupModel("Grupo1", "Meu teste de grupo2", 2, 4, null))
//        adapter.insertGroupList(GroupModel("Grupo1", "Meu teste de grupo1", 1, 2, null))
    }
}