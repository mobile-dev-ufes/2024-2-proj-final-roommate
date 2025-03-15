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

    fun getGroupsFromAdvertisementId(advertisementId: String) {
        val db = FirebaseFirestore.getInstance()

        // Reference to the advertisement document
        val advertisementRef = db.collection("advertisement").document(advertisementId)

        // Fetch the advertisement document to get the "groups" field
        advertisementRef.get()
            .addOnSuccessListener { advertisementDocument ->
                // Check if the "groups" field exists and contains a list of DocumentReferences
                val groupRefs = advertisementDocument.get("groups") as? List<DocumentReference>

                if (groupRefs != null && groupRefs.isNotEmpty()) {
                    // Log the groupRefs to ensure they are being retrieved correctly
                    Log.d("Firebase", "Retrieved group references: $groupRefs")

                    // Now, iterate through the groupRefs and fetch each group document
                    val groupsList = mutableListOf<GroupModel>()
                    groupRefs.forEach { groupRef ->
                        // Fetch each group document by its reference
                        groupRef.get()
                            .addOnSuccessListener { groupDocument ->
                                val groupName = groupDocument.getString("name") ?: ""
                                val groupDescription = groupDocument.getString("description") ?: ""
                                val qttMembers = (groupDocument.get("qttMembers") as? Long)?.toInt() ?: 0  // Ensure qttMembers is an Int
                                val group = GroupModel(
                                    name = groupName,
                                    description = groupDescription,
                                    advertisementId = advertisementId,
                                    qttMembers = qttMembers
                                )

                                groupsList.add(group)

                                // If all groups have been fetched, update the RecyclerView
                                if (groupsList.size == groupRefs.size) {
                                    // Here, you call the adapter's update method to update the RecyclerView
                                    adapter.updateGroupList(groupsList)
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("Firebase", "Error getting group document: ", exception)
                            }
                    }
                } else {
                    Log.w("Firebase", "No valid group references found for advertisement ID: $advertisementId")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firebase", "Error getting advertisement document: ", exception)
            }
    }
}