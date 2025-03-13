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
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.FragmentMyGroupsBinding
import com.example.roommate.ui.adapters.ListMyGroupAdapter
import com.example.roommate.ui.viewModels.GroupViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FragmentMyGroups : Fragment(R.layout.fragment_my_groups) {
    private lateinit var binding: FragmentMyGroupsBinding
    private lateinit var adapter : ListMyGroupAdapter
    private val viewModel: GroupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMyGroupsBinding.inflate(inflater, container, false)

        adapter = ListMyGroupAdapter{
            findNavController().navigate(R.id.action_fragmentMyGroups_to_fragmentGroup)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

        // Apenas para teste do Recycle View
        // adapter.insertGroupList(GroupModel("Meu teste de grupo", 1, 2))


        fetchGroups()
        addGroup()

    }

    private fun addGroup() {
        val db = Firebase.firestore

        val groupMap = hashMapOf(
            "description" to "Meu teste de grupo",
            "qttMembers" to 7,
            "qttNotifications" to 20
        )

        db.collection("group")
            .add(groupMap)
            .addOnSuccessListener { documentRef ->
                println("Group added with ID: ${documentRef.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding group: $e")
            }
    }

    private fun fetchGroups() {
        val db = Firebase.firestore
        val userRef = db.collection("user").document("daniel@gmail.com")

        db.collection("group")
            .whereArrayContains("users", userRef)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    println("Error: $error")
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val id = document.id
                        val description = document.getString("description") ?: ""
                        val qttMembers = document.getLong("qttMembers")?.toInt() ?: 0
                        val qttNotifications = document.getLong("qttNotifications")?.toInt() ?: 0

                        println("Updated Group: $id - $description - $qttMembers - $qttNotifications")
                        adapter.insertGroupList(GroupModel(description, qttMembers, qttNotifications))
                    }
                }
            }
    }
}