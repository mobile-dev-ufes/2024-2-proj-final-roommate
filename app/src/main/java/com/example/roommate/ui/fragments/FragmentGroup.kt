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
    private var argsGroup: GroupModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentGroupBinding.inflate(inflater, container, false)

        argsGroup = arguments?.getSerializable("group") as? GroupModel

        adapter = ListMemberAdapter { user ->  // Corrected: Using ListMemberAdapter
            findNavController().navigate(R.id.action_fragmentGroup_to_fragmentVisitProfile)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

        binding.groupNameTv.text = argsGroup?.name
        binding.groupDescriptionTv.text = argsGroup?.description
        binding.groupQttMembersTv.text = "Members: ${argsGroup?.qttMembers}"  // Display members count

        ///////////////// Get List of Users /////////////////////////////////
        val listaUsuarios = mutableListOf<UserModel>()
        val db = FirebaseFirestore.getInstance()

        var contagem = 0
        val totalUsuarios = argsGroup?.users?.size ?: 0

        argsGroup?.users?.forEach { userRef ->
            userRef.get().addOnSuccessListener { document ->
                document?.let {
                    val name = it.getString("name") ?: ""
                    val bio = it.getString("bio") ?: ""

                    val user = UserModel(name, bio)
                    listaUsuarios.add(user)

                    // Exibe o nome do usuário carregado
                    println("Usuário carregado: $name")
                }

                contagem++
                if (contagem == totalUsuarios) {
                    adapter.updateMemberList(listaUsuarios)
                    println("Todos os usuários carregados.")
                }
            }.addOnFailureListener {
                contagem++
                if (contagem == totalUsuarios) {
                    println("Falha ao carregar alguns usuários.")
                }
            }
        }
    }


}