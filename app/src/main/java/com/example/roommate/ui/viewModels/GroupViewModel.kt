package com.example.roommate.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.GroupModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference

class GroupViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _groups = MutableLiveData<List<GroupModel>>()
    val groups: LiveData<List<GroupModel>> = _groups

    fun fetchUserGroups(userId: String) {
        val userRef: DocumentReference = db.collection("users").document(userId) // Create user reference

        db.collection("groups")
            .whereArrayContains("users", userRef) // Query groups where user is a member
            .get()
            .addOnSuccessListener { querySnapshot ->
                val groupList = mutableListOf<GroupModel>()

                for (document in querySnapshot) {
                    val id = document.id
                    val name = document.getString("name") ?: ""
                    val description = document.getString("description") ?: ""
                    val qttMembers = document.getLong("qttMembers")?.toInt() ?: 0
                    val qttNotifications = document.getLong("qttNotifications")?.toInt() ?: 0

                    groupList.add(GroupModel(description, qttMembers, qttNotifications))
                }

                _groups.value = groupList // Update LiveData
            }
            .addOnFailureListener { e ->
                println("Error fetching groups: $e")
            }
    }
}
