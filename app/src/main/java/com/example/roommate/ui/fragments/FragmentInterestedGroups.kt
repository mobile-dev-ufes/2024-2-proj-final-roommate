package com.example.roommate.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.FragmentInterestedGroupsBinding
import com.example.roommate.ui.adapters.ListInterestedGroupAdapter
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.atomic.AtomicInteger

class FragmentInterestedGroups : Fragment(R.layout.fragment_interested_groups) {
    private lateinit var binding: FragmentInterestedGroupsBinding
    private lateinit var adapter: ListInterestedGroupAdapter
    private val args: FragmentInterestedGroupsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentInterestedGroupsBinding.inflate(inflater, container, false)

        adapter = ListInterestedGroupAdapter { group ->
            val action = FragmentInterestedGroupsDirections
                .actionFragmentInterestedGroupsToFragmentGroup(group) // Pass the group to the next fragment
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val advertisementId = args.advertisementId
        val userId = args.userId
        Log.d("Navigation", "Ad ID: $advertisementId, User ID: $userId")

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

        getGroupsFromAdvertisementId(advertisementId)

        binding.createGroupBtn.setOnClickListener {
            val action = FragmentInterestedGroupsDirections
                .actionFragmentInterestedGroupsToDialogCreateGroup(advertisementId, userId)
            findNavController().navigate(action)
        }
    }

    private fun getGroupsFromAdvertisementId(advertisementId: String) {
        val db = FirebaseFirestore.getInstance()

        // Reference to the advertisement document
        val advertisementRef = db.collection("advertisement").document(advertisementId)

        // Fetch the advertisement document to get the "groups" field
        advertisementRef.get()
            .addOnSuccessListener { advertisementDocument ->
                val groupRefs = (advertisementDocument.get("groups") as? List<*>)?.mapNotNull { it as? DocumentReference }

                if (groupRefs.isNullOrEmpty()) {
                    Log.w("Firebase", "No valid group references found for advertisement ID: $advertisementId")
                    return@addOnSuccessListener
                }

                Log.d("Firebase", "Retrieved group references: $groupRefs")

                val groupsList = mutableListOf<GroupModel>()
                val remainingRequests = AtomicInteger(groupRefs.size)

                groupRefs.forEach { groupRef ->
                    groupRef.get()
                        .addOnSuccessListener { groupDocument ->
                            val groupName = groupDocument.getString("name").orEmpty()
                            val groupDescription = groupDocument.getString("description").orEmpty()
                            val qttMembers = (groupDocument.get("qttMembers") as? Long)?.toInt() ?: 0
                            val isPrivate = groupDocument.getBoolean("isPrivate") ?: false


                            val group = GroupModel(
                                id = groupDocument.id,
                                name = groupName,
                                description = groupDescription,
                                advertisementId = advertisementId,
                                qttMembers = qttMembers,
                                isPrivate = isPrivate
                            )

                            synchronized(groupsList) { groupsList.add(group) }

                            if (remainingRequests.decrementAndGet() == 0) {
                                adapter.updateGroupList(groupsList)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firebase", "Error fetching group document: $groupRef", exception)
                            if (remainingRequests.decrementAndGet() == 0) {
                                adapter.updateGroupList(groupsList)
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error getting advertisement document: ", exception)
            }


    }
}