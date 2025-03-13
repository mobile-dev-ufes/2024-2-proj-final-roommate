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
import com.example.roommate.databinding.FragmentMyGroupsBinding
import com.example.roommate.ui.adapters.ListMyGroupAdapter

class FragmentMyGroups : Fragment(R.layout.fragment_my_groups) {
    private lateinit var binding: FragmentMyGroupsBinding
    private val adapter = ListMyGroupAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMyGroupsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

        // Apenas para teste do Recycle View
        adapter.insertGroupList(GroupModel("Meu teste de grupo", 1, 2))
        adapter.insertGroupList(GroupModel("Meu teste de grupo2", 2, 4))
        adapter.insertGroupList(GroupModel("Meu teste de grupo3", 3, 6))

        /*
        binding.adGroupBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdvertisement_to_fragmentInterestedGroups)
        }
        */
    }
}