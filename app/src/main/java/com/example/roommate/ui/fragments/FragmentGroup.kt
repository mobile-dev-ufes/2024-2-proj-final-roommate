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
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.FragmentGroupBinding
import com.example.roommate.ui.adapters.ListMemberAdapter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FragmentGroup : Fragment(R.layout.fragment_group) {
    private lateinit var binding: FragmentGroupBinding
    private lateinit var adapter: ListMemberAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentGroupBinding.inflate(inflater, container, false)
        adapter = ListMemberAdapter {
            findNavController().navigate(R.id.action_fragmentGroup_to_fragmentVisitProfile)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

        // Apenas para testar o Recycle View
        adapter.insertMemberList(UserModel("Daniel1", "A vida é bela1"))
        adapter.insertMemberList(UserModel("Daniel2", "A vida é bela2"))
        adapter.insertMemberList(UserModel("Daniel3", "A vida é bela3"))

        fetchUsers()
    }

    private fun fetchUsers() {
        val db = Firebase.firestore

        /* val groupMap = hashMapOf(
            "name" to "TesteGrupo",
            "description" to "Descricao"
        )

        db.collection("group")
            .add(groupMap)
            .addOnSuccessListener { documentRef ->
                println("Group added with ID: ${documentRef.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding group: $e")
            } */

        /* db.collection("group")
            .document("w6FkvkuwfQZnbDPJ7Bnd")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name")
                    val description = document.getString("description")
                    println("Group: $name - $description")
                } else {
                    println("No such group found!")
                }
            }
            .addOnFailureListener { e ->
                println("Error fetching group: $e")
            } */
    }
}