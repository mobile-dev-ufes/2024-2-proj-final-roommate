package com.example.roommate.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.GroupModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class GroupViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _groups = MutableLiveData<List<GroupModel>>()
    val groups: LiveData<List<GroupModel>> = _groups

    fun fetchUserGroups(userId: String) {
        val userRef: DocumentReference = db.collection("user").document(userId)

        db.collection("group")
            .whereArrayContains("users", userRef)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val groupList = mutableListOf<GroupModel>()

                for (document in querySnapshot) {
                    val id = document.id
                    val name = document.getString("name") ?: ""
                    val description = document.getString("description") ?: ""
                    //val qttMembers = document.getLong("qttMembers")?.toInt() ?: 0

                    val qttNotifications = document.getLong("qttNotifications")?.toInt() ?: 0

                    // Get the 'users' field which is a List of DocumentReferences
                    val usersRef = document.get("users") as? List<*> ?: emptyList<DocumentReference>()

                    // Convert the List<*> to List<DocumentReference> safely
                    val userRefsList = usersRef.mapNotNull { it as? DocumentReference }
                    val qttMembers = userRefsList.size

                    // Create the GroupModel with the list of DocumentReference
                    groupList.add(GroupModel(name, description, qttMembers, qttNotifications, userRefsList))
                }

                _groups.postValue(groupList)
            }
            .addOnFailureListener { e ->
                println("Error fetching groups: $e")
            }
    }
}
